package com.edupay.commons.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * com.caicui.commons.utils
 * Created by yukewi on 2016/1/20 17:23.
 */
public class ResultMapUtils {

    /**
     * 失败返回值
     *
     * @param message
     * @return
     */
    public static Map<String, Object> error(String message) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("state", "error");
        map.put("msg", message);
        return map;
    }

    /**
     * 成功返回值
     *
     * @param data
     * @return
     */
    public static Map<String, Object> success(Object data) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("state", "success");
        map.put("msg", null);
        map.put("data", data);
        return map;
    }

    /**
     * 成功返回值
     *
     * @param dataMap
     * @return
     */
    public static Map<String, Object> successMap(Map<String, Object> dataMap) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("state", "success");
        map.put("msg", null);
        map.put("data", dataMap);
        return map;
    }


}
