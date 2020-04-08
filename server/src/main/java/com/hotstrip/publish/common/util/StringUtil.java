package com.seestech.sell.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by idiot on 2017/1/16.
 */
public class StringUtils {
    private static Logger logger = LoggerFactory.getLogger(StringUtils.class);

    /**
     * @description 生成uuid
     * @return
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 字符串替换
     * @param template
     * @param params
     * @return
     */
    public static String simpleTemplate(String template, Map<String, Object> params){
        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile("\\$\\{\\w+\\}").matcher(template);
        while (m.find()) {
            String param = m.group();
            Object value = params.get(param.substring(2, param.length() - 1));
            m.appendReplacement(sb, value == null ? "" : value.toString());
        }
        m.appendTail(sb);
        return sb.toString();
    }


    /**
     * @description  数组转字符串
     * @param objects       数组
     * @param option        分隔符
     * @return
     */
    public static String arrayToString(Object[] objects, String option){
        //判断分隔符是否为空    设置默认值
        if(option == null || "".equals(option))
            option = ";";
        StringBuffer buffer = new StringBuffer();
        for (Object o : objects){
            if(o != null || !"".equals(o))
                buffer.append(o).append(option);
        }
        int index = buffer.lastIndexOf(option);
        return index < 0 ? "" : buffer.substring(0, index);
    }

    public static String arrayToString(int[] objects, String option){
        //重载方法
        if(option == null || "".equals(option))
            option = ";";
        StringBuffer buffer = new StringBuffer();
        for (Object o : objects){
            if(o != null || !"".equals(o))
                buffer.append(o).append(option);
        }
        int index = buffer.lastIndexOf(option);
        return index < 0 ? "" : buffer.substring(0, index);
    }


    /**
     * @description  字符串转换成数组
     * @param objects
     * @param param
     * @param option
     * @return
     */
    public static int[] stringToArray(int[] objects, String param, String option){
        //非空判断
        if(param == null || "".equals(param)){
            return objects;
        }
        //判断分隔符是否为空    设置默认值
        if(option == null || "".equals(option))
            option = ";";
        //根据param分割字符串  转换成数组
        String[] temp = param.split(option);
        objects = toIntArray(temp, temp.length);
        return objects;
    }

    public static int[] toIntArray(String[] objects, int length){
        int[] result = new int[length];
        //判断当前的数组是否为空  以及  元素个数是否小于1
        if(objects == null || objects.length < 1){
            for (int i = 0; i < length; i++) {
                result[i] = 0;
            }
        }else {
            for (int i = 0; i < objects.length; i++) {
                if(i<length) {
                    result[i] = Integer.parseInt(objects[i]);
                }
            }
        }
        return result;
    }

    public static String[] toStringArray(int[] objects){
        String[] result = new String[objects.length];
        for (int i = 0; i < objects.length; i++) {
            result[i] = String.valueOf(objects[i]);
        }
        return result;
    }

    //随机数
    public static String randomValue(int seed){
        return String.valueOf((int)((Math.random()*9+1)*seed));
    }

    /**
     * 去掉左边空格
     * @param s
     * @return
     */
    public static String ltrim(String s) {
        int i = 0;
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        return s.substring(i);
    }

    /**
     * 去掉右边空格
     * @param s
     * @return
     */
    public static String rtrim(String s) {
        int i = s.length()-1;
        while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
            i--;
        }
        return s.substring(0, i+1);
    }


    /**
     * 获取String类型 value值
     *
     * @param srcTest
     * @param keys
     * @return
     */
    public static Map<String, String> StringToJSONGetValue(String srcTest, String ... keys) {
        JSONObject json = JSONObject.parseObject(srcTest);
        Map<String, String> data = new HashMap();
        for (String key : keys) {
            String info = json.getString(key);
            if (info == null) {
                logger.error("key值取值为 null ,请核实key值");
                return null;
            }
            data.put(key, info);
        }
        return data;
    }

}
