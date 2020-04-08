package com.hotstrip.publish.web;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.hotstrip.publish.common.annotation.NotNullParam;
import com.hotstrip.publish.common.annotation.RequireToken;
import com.hotstrip.publish.common.util.Const;
import com.hotstrip.publish.common.util.JsonUtil;
import com.hotstrip.publish.model.Directory;
import com.hotstrip.publish.model.FileInfo;
import com.hotstrip.publish.model.R;
import com.hotstrip.publish.model.User;
import com.hotstrip.publish.service.DirectoryService;
import com.hotstrip.publish.service.FileService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DirectoryController extends SuperController {

    @Resource
    private DirectoryService directoryService;
    @Resource
    private FileService fileService;

    /**
     * 查询根目录
     * @param token
     * @return
     */
    @RequireToken
    @PostMapping(value = "/web/directory/root-list")
    public Object rootList(@RequestHeader(value = Const.TOKEN) String token) {
        // 获取用户信息
        User user = getUser(getUserIdByToken(token));
        if(user == null){
            return R.error(Const.ERROR_PARAM, "invalid user");
        }
        Directory info = Directory.builder()
                .rootFlag(1) // 根目录
                .build();
        Page<Directory> list = directoryService.getDirectories(new RowBounds(), info);
        JSONArray objects = JsonUtil.parseArray(list);
        return R.ok("success").put("data", objects).put("totalCount", list.getTotal());
    }

    /**
     * 查询子级目录
     * @param parentDirectoryId
     * @return
     */
    @RequireToken
    @NotNullParam(params = {"parentDirectoryId"})
    @PostMapping(value = "/web/directory/child-list")
    public Object childList(Long parentDirectoryId) {
        Directory info = Directory.builder()
                .parentDirectoryId(parentDirectoryId)
                .build();
        Page<Directory> list = directoryService.getDirectories(new RowBounds(), info);
        JSONArray objects = JsonUtil.parseArray(list);
        return R.ok("success").put("data", objects).put("totalCount", list.getTotal());
    }

    /**
     * 编辑目录
     * @param directoryId
     * @param directoryName
     * @return
     */
    @RequireToken
    @NotNullParam(params = {"directoryName"})
    @PostMapping(value = "/web/directory/edit")
    public Object edit(Long directoryId,
                       String directoryName,
                       Long parentDirectoryId) {
        Directory info = Directory.builder()
                .directoryId(directoryId)
                .directoryName(directoryName)
                .parentDirectoryId(parentDirectoryId)
                .build();
        if (null == directoryId) {
            // 新增
            directoryService.insert(info);
        } else {
            // 修改
            directoryService.update(info);
        }
        return R.ok("success");
    }

    /**
     * 删除目录
     * @param directoryId
     * @return
     */
    @RequireToken
    @NotNullParam(params = {"directoryId"})
    @PostMapping(value = "/web/directory/delete")
    public Object delete(Long directoryId) {
        FileInfo info = FileInfo.builder()
                .directoryId(directoryId)
                .build();
        // 目录下有文件不能删除
        Page<FileInfo> list = fileService.getFiles(new RowBounds(0, 1), info);
        if (list.size() > 0) {
            return R.error(Const.ERROR_PARAM, "cant't delete");
        }
        // 删除目录
        directoryService.deleteByDirectoryId(directoryId);
        return R.ok("success");
    }
}
