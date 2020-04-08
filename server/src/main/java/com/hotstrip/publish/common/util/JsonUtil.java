package com.hotstrip.publish.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.ValueFilter;

import java.util.List;


public class JsonUtil {

    /**
     * @description  json 过滤
     * @return
     */
    public static ValueFilter initValueFilter(final String ... params){
        // 把long类型的转换为字符串
        ValueFilter valueFilter = (o, name, value) -> {
            if(value instanceof Long){
                return String.valueOf(value);
            }
            for (String param : params){
                if(name.equals(param) && value == null){
                    return "";
                }
            }
            return value;
        };
        return valueFilter;
    }

    /**
     * @descripton   list -->  json 过滤之后的 jsonArray
     * @param list
     * @param params   设置过滤的属性  当null时返回空字符串
     * @return
     */
    public static JSONArray parseArray(List list, String ... params){
        // json过滤
        ValueFilter valueFilter = JsonUtil.initValueFilter(params);
        JSONArray object = JSON.parseArray(JSON.toJSONString(list, valueFilter));
        return object;
    }
}
