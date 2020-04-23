package com.hotstrip.publish.web;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.hotstrip.publish.common.ApplicationConfig;
import com.hotstrip.publish.common.annotation.NotNullParam;
import com.hotstrip.publish.common.annotation.RequireToken;
import com.hotstrip.publish.common.util.Const;
import com.hotstrip.publish.common.util.FileUtil;
import com.hotstrip.publish.common.util.JsonUtil;
import com.hotstrip.publish.common.util.StringUtil;
import com.hotstrip.publish.model.Directory;
import com.hotstrip.publish.model.FileInfo;
import com.hotstrip.publish.model.R;
import com.hotstrip.publish.service.DirectoryService;
import com.hotstrip.publish.service.FileService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Iterator;

@RestController
public class FileController extends SuperController {

    @Resource
    private FileService fileService;
    @Resource
    private DirectoryService directoryService;
    @Resource
    private ApplicationConfig applicationConfig;

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @param directoryId
     * @param oldName
     * @param fileType
     * @return
     */
    @RequireToken
    @PostMapping(value = "/web/file/list")
    public Object list(@RequestParam(defaultValue = "1") Integer pageNo,
                       @RequestParam(defaultValue = "20") Integer pageSize,
                       Long directoryId,
                       String oldName,
                       Integer fileType) {
        FileInfo info = FileInfo.builder()
                .directoryId(directoryId)
                .oldName(oldName)
                .fileType(fileType)
                .build();
        Page<FileInfo> list = fileService.getFiles(new RowBounds((pageNo - 1) * pageSize, pageSize), info);
        JSONArray objects = JsonUtil.parseArray(list);
        return R.ok("success").put("data", objects).put("totalCount", list.getTotal());
    }

    /**
     * 编辑
     * @param fileId
     * @param oldName
     * @return
     */
    @RequireToken
    @NotNullParam(params = {"fileId", "oldName"})
    @PostMapping(value = "/web/file/edit")
    public Object edit(Long fileId,
                       String oldName) {
        FileInfo info = FileInfo.builder()
                .fileId(fileId)
                .oldName(oldName)
                .build();
        fileService.update(info);
        return R.ok("success");
    }

    /**
     * 删除
     * @param fileId
     * @return
     */
    @RequireToken
    @NotNullParam(params = {"fileId"})
    @PostMapping(value = "/web/file/delete")
    public Object delete(Long fileId) {
        fileService.delete(fileId);
        return R.ok("success");
    }

    /**
     * 上传文件
     * @param identifier 文件的id  之后用于临时目录
     * @param chunkNumber 分片数 从 1 开始
     * @param totalChunks 总分片数
     * @param fileType 文件类型 1 图片 2 视频
     * @param directoryId 目录id
     * @return
     */
    @RequireToken
    @NotNullParam(params = {"identifier", "directoryId"})
    @PostMapping(value = "/web/file/simpleUpload")
    public Object simpleUpload(HttpServletRequest httpServletRequest,
                               String identifier,
                               Integer chunkNumber,
                               Integer totalChunks,
                               Integer fileType,
                               Long directoryId){
        // 获取目录信息
        Directory directory = directoryService.getDirectoryByDirectoryId(directoryId);
        if (directory == null) {
            logger.error("/web/file/simpleUpload......directoryId: [{}]", directoryId);
            return R.error(Const.ERROR_PARAM, "invalid param directoryId");
        }
        // 根据目录id获取目录路径
        String directoryPath = directoryService.getDirectoryPathByDirectoryId(directoryId);
        String uploadPath = FileUtil.addPathSeparate(applicationConfig.getBaseDirectory(), directoryPath, identifier);
        logger.info("/web/file/simpleUpload......upload path: [{}]", uploadPath);

        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) httpServletRequest;
        // 取得request中的所有文件名
        Iterator<String> iterator = multiRequest.getFileNames();
        try {
            while(iterator.hasNext()) {
                //取得上传文件
                MultipartFile file = multiRequest.getFile(iterator.next());
                if (file != null) {
                    // 取得当前上传文件的文件名称
                    String myFileName = file.getOriginalFilename();
                    // 临时目录用来存放所有分片文件
                    File tempFileDir = new File(FileUtil.buildFileByPath(uploadPath), identifier);

                    boolean uploadDone = isUploadFileDone(file, tempFileDir, myFileName, chunkNumber, totalChunks);

                    // 所有分片文件都上传完成  将所有分片文件合并到一个文件中  得到 destTempFile 就是最终的文件  然后保存即可
                    if (uploadDone) {
                        File destTempFile = new File(uploadPath, myFileName);
                        for (int i = 0; i < totalChunks; i++) {
                            File partFile = new File(tempFileDir, myFileName + "_" + i + ".part");
                            // 合并分片文件
                            FileUtil.copyFile(partFile, destTempFile);
                        }
                        // 获取文件拓展名
                        String extName = FileUtil.getExtName(myFileName);
                        // 重命名文件
                        String newFileName = System.currentTimeMillis() + StringUtil.randomValue(10000)+ "." + extName;
                        File finalFile = new File(uploadPath, newFileName);
                        if(destTempFile.renameTo(finalFile)){
                            logger.info("/web/file/simpleUpload.json......rename file.........new file name: [{}]", finalFile.getName());
                        }

                        // 删除临时文件夹 以及里面的文件
                        FileUtil.deleteDirectory(tempFileDir);

                        String result = FileUtil.removeBaseDirectory(applicationConfig.getBaseDirectory(), finalFile.getPath());
                        result = FileUtil.addPathSeparate(getServerPath(httpServletRequest), result);
                        logger.info("result: [{}]", result);

                        // 新增文件
                        FileInfo info = FileInfo.builder()
                                .directoryId(directoryId)
                                .fileType(fileType)
                                .oldName(myFileName)
                                .fileName(newFileName)
                                .filePath(result)
                                .fileSize(finalFile.length())
                                .build();
                        fileService.insert(info);
                    } else {
                        // 临时文件创建失败  删除临时文件夹 以及里面的文件
                        if (chunkNumber == totalChunks) {
                            FileUtil.deleteDirectory(tempFileDir);
                            logger.error("/web/file/simpleUpload......upload file failed......part file error......temp directory path: [{}]", tempFileDir.getPath());
                            return R.error(Const.ERROR_PARAM, "upload file failed");
                        }
                    }
                }
            }
        } catch (Exception e){
            logger.error("/web/file/simpleUpload......upload file failed......msg: [{}]......cause: [{}]", e.getMessage(), e.getCause());
            return R.error(Const.ERROR_PARAM, "upload file failed");
        }
        return R.ok("upload file success");
    }

    // 获取服务地址
    private String getServerPath(HttpServletRequest httpServletRequest) {
        return new StringBuffer(httpServletRequest.getScheme())
                .append("://")
                .append(httpServletRequest.getServerName())
                .append(":" )
                .append(httpServletRequest.getServerPort())
                .append(httpServletRequest.getContextPath())
                .toString();
    }

}
