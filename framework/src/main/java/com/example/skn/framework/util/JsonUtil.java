package com.example.skn.framework.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by hf
 */
public class JsonUtil {
    public static String toJson(Object object) {
        Object o = JSONObject.toJSON(object);
        if (o != null)
            return o.toString();
        return null;
    }

    public static String toJson(List<Object> object) {
        Object o = JSONArray.toJSON(object);
        if (o != null)
            return o.toString();
        return null;
    }

    public static <T> T fromJson(String json, Class<T> tClass) {
        return JSONObject.parseObject(json, tClass);
    }

    public static <T> List<T> fromArray(String json, Class<T> tClass) {
        return JSONArray.parseArray(json, tClass);
    }
}
