package com.edupay.commons.assertion;

import org.apache.commons.lang3.StringUtils;

public class Assert {

    /**
     * 断言此表达式为假
     *
     * @param message
     * @param condition
     */
    public static void assertFalse(String message, boolean condition) {
        assertTrue(message, !condition);
    }

    /**
     * 断言此对象不为空
     * 若此对象为空字符串 此断言也失败
     *
     * @param message
     * @param object
     */
    public static void assertNotNull(String message, Object object) {
        if (object instanceof String) {
            assertTrue(message, object != null && StringUtils.isNotEmpty((String) object));
        }
        assertTrue(message, object != null);
    }

    /**
     * 断言此对象不为空
     * 若此对象为空字符串 此断言也失败
     *
     * @param message
     * @param objects
     */
    public static void assertNotNull(String message, Object[]... objects) {
        for (Object[] object : objects) {
            assertNotNull(message, object);
        }
    }
    
    /**
     * 断言此对象不为空
     * 若此对象为空字符串 此断言也失败
     *
     * @param message
     * @param objects
     */
    public static void assertNotNull(String message, Object... objects) {
        for (Object object : objects) {
            assertNotNull(message, object);
        }
    }

    /**
     * 断言此对象为空
     * 若此对象为空字符串 此断言也有效
     *
     * @param message
     * @param object
     */
    public static void assertNull(String message, Object object) {
        if (object instanceof String) {
            assertTrue(message, object == null || StringUtils.isEmpty((String) object));
        }
        assertTrue(message, object == null);
    }

    /**
     * 断言此对象为空
     * 若此对象为空字符串 此断言也有效
     *
     * @param message
     * @param objects
     */
    public static void assertNull(String message, Object... objects) {
        for (Object object : objects) {
            assertNull(message, object);
        }
    }


    /**
     * 断言条件为真
     *
     * @param message
     * @param condition
     * @return
     */
    public static void assertTrue(String message, boolean condition) {
        if (!condition) {
            fail(message);
        }
    }

    /**
     * 判断条件失效，并抛出异常
     *
     * @param message
     */
    private static void fail(String message) {
        if (message == null) {
            throw new RuntimeException();
        }
        throw new RuntimeException(message);
    }

}
