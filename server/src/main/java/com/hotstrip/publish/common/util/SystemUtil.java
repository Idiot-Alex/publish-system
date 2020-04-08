package com.seestech.sell.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;


/**
 * Created by idiot on 2017/4/10.
 * @description  获取系统相关信息
 */
public class SystemUtils {
    private static Logger logger = LoggerFactory.getLogger(SystemUtils.class);

    private static String OS = System.getProperty("os.name").toLowerCase();

    private static String Windows = "windows";

    /**
     * @description 判断当前系统是否是windows
     * @return
     */
    public static boolean isWindows(){
        return OS.indexOf(Windows) >= 0;
    }

    /**
     * 获取当前系统的jdk版本
     * @return
     */
    public static String getJavaVersion(){
        String javaVersion = System.getProperty("java.version");
        return javaVersion.substring(0, 3);
    }

    /**
     * 获取MAC地址
     * @param ia
     * @return
     * @throws Exception
     */
    public static String getMACAddress(InetAddress ia) throws SocketException {
        //获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

        //下面代码是把mac地址拼装成String
        StringBuffer sb = new StringBuffer();

        for(int i=0;i<mac.length;i++){
            if(i!=0){
                sb.append("-");
            }
            //mac[i] & 0xFF 是为了把byte转化为正整数
            String s = Integer.toHexString(mac[i] & 0xFF);

            sb.append(s.length()==1?0+s:s);
        }

        //把字符串所有小写字母改为大写成为正规的mac地址并返回
        return sb.toString().toUpperCase();
    }

}
