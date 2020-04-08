package com.hotstrip.publish.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Created by idiot on 2017/1/16.
 */
public class StringUtil {
    private static Logger logger = LoggerFactory.getLogger(StringUtil.class);

    /**
     * @description 生成uuid
     * @return
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // 随机数
    public static String randomValue(int seed){
        return String.valueOf((int)((Math.random()*9+1)*seed));
    }

}
