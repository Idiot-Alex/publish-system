package com.hotstrip.publish;

import com.hotstrip.publish.service.DirectoryService;
import org.junit.Test;

import javax.annotation.Resource;

public class DirectoryControllerTest extends AppTest {

    @Resource
    private DirectoryService directoryService;

    @Test
    public void testGetDirectoryPath() {
        /*Long directoryId = 1247751770768146432L;
        String path = directoryService.getDirectoryPathByDirectoryId(directoryId);
        logger.info("path: {}", path);*/
    }
}
