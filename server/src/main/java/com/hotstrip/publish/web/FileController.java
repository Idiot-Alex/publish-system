package com.hotstrip.publish.web;

import com.github.pagehelper.Page;
import com.hotstrip.publish.common.annotation.NotNullParam;
import com.hotstrip.publish.common.annotation.RequireToken;
import com.hotstrip.publish.model.FileInfo;
import com.hotstrip.publish.model.R;
import com.hotstrip.publish.service.FileService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class FileController extends SuperController {

    @Resource
    private FileService fileService;

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @param oldName
     * @param fileType
     * @return
     */
    @RequireToken
    @PostMapping(value = "/web/file/list")
    public Object list(@RequestParam(defaultValue = "1") Integer pageNo,
                       @RequestParam(defaultValue = "20") Integer pageSize,
                       String oldName,
                       Integer fileType) {
        FileInfo info = FileInfo.builder()
                .oldName(oldName)
                .fileType(fileType)
                .build();
        Page<FileInfo> list = fileService.getFiles(new RowBounds((pageNo - 1) * pageSize, pageSize), info);
        return R.ok("success").put("data", list).put("totalCount", list.getTotal());
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

    // 上传文件
}
