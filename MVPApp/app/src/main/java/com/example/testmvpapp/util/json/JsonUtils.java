package com.example.testmvpapp.util.json;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * json工具类
 */

public class JsonUtils {

    // 默认日期格式（年月日时分秒）
    public static final String default_dateFormat = "yyyy-MM-dd HH:mm:ss";
    // 存在时间格式（年月日）
    public static final String dateFormat = "yyyy-MM-dd";

    /**
     * 把json string 转化成类对象
     *
     * @param str
     * @param t
     * @return
     */
    public static <T> T parseObject(String str, Class<T> t) {
        try {
            if (str != null && !"".equals(str.trim())) {
                T res = JSON.parseObject(str.trim(), t);
                return res;
            }
        } catch (Exception e) {
            Log.e("数据转换出错", "exception:" + e.getMessage());
        }
        return null;
    }

    /**
     * 把json string 转化成类对象
     *
     * @param str
     * @param t
     * @return
     */
    public static <T> List<T> parseArray(String str, Class<T> t) {
        try {
            if (str != null && !"".equals(str.trim())) {
                List<T> res = JSON.parseArray(str.trim(), t);
                return res;
            }
        } catch (Exception e) {
            Log.e("数据转换出错", "exception:" + e.getMessage());
        }
        return null;
    }

    /**
     * 对象转json字符串，默认不执行进行日期转换
     *
     * @param obj 对象
     * @return
     */
    public static String ObjectTojson(Object obj) {

        return ObjectTojson(obj, false);
    }

    /**
     * 对象转json字符串，使用默认日期转换
     *
     * @param obj 对象
     * @param useDateFormat 自定义时间格式
     * @return
     */
    public static String ObjectTojson(Object obj, boolean useDateFormat) {

        return ObjectTojson(obj, useDateFormat, default_dateFormat);
    }

    /**
     * 自定义日期格式
     * @param obj
     * @param dateFormat
     * @return
     */
    public static String ObjectTojson(Object obj, String dateFormat) {

        return ObjectTojson(obj, true, dateFormat);

    }

    /**
     * 对象转字符串，总处理方法，不对外开放
     * @param obj javabean对象
     * @param useDateFormat
     * @param dateFormat
     * @return
     */
    private static String ObjectTojson(Object obj, boolean useDateFormat, String dateFormat) {
        if (useDateFormat) {
            return JSON.toJSONStringWithDateFormat(obj, dateFormat);
        }
        return JSON.toJSONString(obj);

    }


    /**
     * 把类对象转化成json string
     *
     * @param t
     * @return
     */
    public static <T> String toJson(T t) {
        try {
            return JSON.toJSONString(t);
        } catch (Exception e) {
            Log.e("数据转换出错", "exception:" + e.getMessage());
        }
        return "";
    }

}
