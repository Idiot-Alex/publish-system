package com.hotstrip.publish;

import com.hotstrip.publish.common.util.EncodeMD5;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonTest {
    private static Logger logger = LoggerFactory.getLogger(CommonTest.class);

    @Test
    public void testEncodeMD5() throws Exception {
        String str = "demo123";
        logger.info(EncodeMD5.getMD5Code(str));
    }
}
