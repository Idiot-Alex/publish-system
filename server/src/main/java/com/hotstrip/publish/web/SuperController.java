package com.hotstrip.publish.web;

import com.hotstrip.publish.common.util.Const;
import com.hotstrip.publish.common.util.FileUtil;
import com.hotstrip.publish.common.util.TokenUtil;
import com.hotstrip.publish.model.User;
import com.hotstrip.publish.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

public class SuperController {
    protected static Logger logger = LoggerFactory.getLogger(SuperController.class);

    @Resource
    private UserService userService;

    //获取用户
    protected User getUser(Long userId){
        if (userId == null)
            return null;
        User user = userService.getUserByUserId(userId);
        return user;
    }

    // 根据token获取用户信息编号
    protected Long getUserIdByToken(String token){
        Long userId = null;
        try {
            String decodeToken = TokenUtil.decodeToken(token);
            String[] message = decodeToken.split(Const.HASH_TAG);
            userId = Long.valueOf(message[0]);
        } catch (Exception e) {
            logger.error("decode token failed..........caused: [{}]......msg: [{}]", e.getCause(), e.getMessage());
            //抛出异常比较合适   抛出自定义异常   异常返回json 信息
        }
        return userId;
    }

    /**
     * @description  存储上传的文件 返回是否完成
     * @param file			待上传的文件
     * @param tempFileDir	临时存储的分片文件
     * @param myFileName	文件名
     * @param chunk			当前的“片”
     * @param chunks		总数的“片”
     * @return	是否上传全部的“片”文件
     */
    protected boolean isUploadFileDone(MultipartFile file, File tempFileDir, String myFileName, int chunk, int chunks){
        if (!tempFileDir.exists()) {
            tempFileDir.mkdirs();
        }
        // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台(默认每片为5M)
        File tempPartFile = new File(tempFileDir, myFileName + "_" + (chunk - 1) + ".part");
        // 如果文件存在就不用继续上传了
        if(!tempPartFile.exists())
            FileUtil.copyInputStreamToFile(file, tempPartFile);
        else
            logger.info("current part file already exists，no need upload again...{}_{}.part", myFileName, chunk);

        // 是否全部上传完成 所有分片都存在才说明整个文件上传完成
        boolean uploadDone = true;
        for (int i = 0; i < chunks; i++) {
            File partFile = new File(tempFileDir, myFileName + "_" + i + ".part");
            if (!partFile.exists()) {
                uploadDone = false;
            }
        }
        return uploadDone;
    }
}
