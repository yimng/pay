package com.edupay.commons.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * com.caicui.commons.utils
 * Created by yukewi on 2016/6/29 9:57.
 */
public class StackTraceUtils {

    public static String getMethodInfo() {
        // 获得当前类名
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
        String clazz = stackTraceElement.getClassName();
        // 获得当前方法名
        String method = stackTraceElement.getMethodName();
        return "Class name : " + clazz + " Method Name " + method;
    }

    public static List<String> getStackTraces(Throwable throwable) {
        List<String> message = getStackTrace(throwable);
        for (Throwable tr : throwable.getSuppressed()) {
            message.addAll(getStackTrace(tr));
        }
        return message;
    }

    private static List<String> getStackTrace(Throwable throwable) {
        List<String> message = new LinkedList<>();
        final StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            final String trace = stackTraceElement.toString();
            message.add("\t at " + trace);
        }
        Collections.reverse(message);
        message.add("Cause by : " + throwable);
        return message;
    }


}

