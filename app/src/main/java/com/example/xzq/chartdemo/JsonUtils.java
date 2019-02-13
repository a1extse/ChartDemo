package com.example.xzq.chartdemo;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Description : JSON工具类（目前暂添加了Gson解析，后续再逐渐接入其它第三方解析）
 *
 * @author : xzq
 * @version : 1.0
 *          Create Date : 2015-8-19 上午10:28:36
 **/
public class JsonUtils {

    /**
     * Pojo to json
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        try {
            Gson gson = new Gson();
            return gson.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * json to pojo
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json to pojo
     *
     * @param json
     * @param typeOfT
     * @return
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}