package com.hotstrip.publish.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by idiot on 2017/1/3.
 * @description 生成token  以及对token的加密   解密
 * 仅仅对 PC 端 WEB 页面接口生效
 */
public class TokenUtil {
    private static Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    private static final String key = "Chimoo2019!@#ABC";

    public static String encodeToken(String message){
        try {
            // AES 加密生成 Token
            return AESUtil.Encrypt(message, key);
        }catch (Exception e){
            logger.error("token get failed...message: [{}]", e.getMessage());
            return null;
        }
    }

    public static String decodeToken(String token) throws Exception {
        // AES 解密
        return AESUtil.Decrypt(token, key);
    }

    /**
     * @description 根据token的创建时间  有效时间  和当前时间  判断改token是否失效
     * @param tokenTime
     * @param TokenValidTime
     * @return  true 未失效   false 失效
     */
    public static boolean validToken(long tokenTime, long TokenValidTime){
        boolean valid = false;
        long currentTime = System.currentTimeMillis();
        if(tokenTime + TokenValidTime > currentTime){
            valid = true;
        }
        return valid;
    }
}
