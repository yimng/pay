package com.edupay.commons.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * com.bitzh.util
 * Created by yukewi on 2015/10/23 15:51.
 */
public class BeanMapUtils {

    /**
     * 将对象拆分成MAP 然后不做任何处理
     *
     * @param object
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static Map<String, Object> describe(Object object) {
        Map<String, Object> map = new LinkedHashMap<>();
        try {
            if (object != null) {
                final Map describe = PropertyUtils.describe(object);
                for (Object key : describe.keySet()) {
                    if (key != null && describe.get(key) != null) {
                        final Object value = describe.get(key);
                        map.put(key.toString(), value);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //属性class也作为返回值但是我们一般不用
        map.remove("class");
        return map;
    }


    /**
     * 将对象拆分成MAP
     * <p/>
     * 替换属性名
     * <p/>
     * 然后删除不需要展示的属性
     *
     * @param object
     * @return
     */
    public static Map<String, Object> describe(Object object, Map<String, String> replacePropertyMap, String[] removeProperties) {
        Map<String, Object> map = describe(object);
        map = replace(map, replacePropertyMap);
        map = remove(map, removeProperties);
        return map;
    }

    /**
     * 将对象拆分成MAP
     * <p/>
     * 替换属性名
     *
     * @param object
     * @param replacePropertyMap
     * @return
     */
    public static Map<String, Object> describe(Object object, Map<String, String> replacePropertyMap) {
        return describe(object, replacePropertyMap, null);
    }


    /**
     * 将对象拆分成MAP
     * <p/>
     * 删除属性名
     *
     * @param object
     * @param removeProperties
     * @return
     */
    public static Map<String, Object> describe(Object object, String[] removeProperties) {
        return describe(object, null, removeProperties);
    }

    /**
     * 将集合中的对象逐一分解成MAP
     *
     * @param collection
     * @param replacePropertyMap
     * @param removeProperties
     * @return
     */
    public static List<Map<String, Object>> describeCollection(Collection<?> collection, Map<String, String> replacePropertyMap, String[] removeProperties) {
        List<Map<String, Object>> list = new LinkedList<>();
        if (CollectionUtils.isEmpty(collection)) {
            return list;
        }
        for (Object o : collection) {
            Map<String, Object> describe = describe(o);
            if (describe != null && CollectionUtils.isNotEmpty(describe.keySet())) {
                describe = replace(describe, replacePropertyMap);
                describe = remove(describe, removeProperties);
                list.add(describe);
            }
        }
        return list;
    }

    /**
     * 将集合中的对象逐一分解成MAP
     *
     * @param collection
     * @param replacePropertyMap
     * @return
     */
    public static List<Map<String, Object>> describeCollection(Collection<?> collection, Map<String, String> replacePropertyMap) {
        return describeCollection(collection, replacePropertyMap, null);
    }

    /**
     * 将集合中的对象逐一分解成MAP
     *
     * @param collection
     * @param removeProperties
     * @return
     */
    public static List<Map<String, Object>> describeCollection(Collection<?> collection, String[] removeProperties) {
        return describeCollection(collection, null, removeProperties);
    }

    /**
     * 将集合中的对象逐一分解成MAP
     *
     * @param collection
     * @return
     */
    public static List<Map<String, Object>> describeCollection(Collection<?> collection) {
        return describeCollection(collection, null, null);
    }

    private static Map<String, Object> remove(Map<String, Object> map, String[] removeProperties) {
        if (removeProperties == null) {
            return map;
        }

        // 删除属性
        for (String removeProperty : removeProperties) {
            map.remove(removeProperty);
        }
        return map;
    }

    private static Map<String, Object> replace(Map<String, Object> map, Map<String, String> replacePropertyMap) {
        if (replacePropertyMap == null) {
            return map;
        }
        // 替换属性
        for (Map.Entry<String, String> entry : replacePropertyMap.entrySet()) {
            final String key = entry.getKey();
            final String replacement = entry.getValue();
            final Object value = map.get(key);
            map.put(replacement, value);
            map.remove(key);
        }
        return map;
    }
}
