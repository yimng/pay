package com.edupay.commons.condition;


import java.util.ArrayList;
import java.util.List;

import com.edupay.commons.assertion.Assert;

/**
 * com.caicui.commons.condition
 * Created by yukewi on 2015/8/6 11:56.
 */
public class ConditionBuilderTest {
    /**
     * 主函数入口方法
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Object[] objects = new Object[]{
                true, "hello",
                Boolean.FALSE, "sss",
                true, "hello",
                Boolean.FALSE, "sss",
                true, "hello",
                Boolean.FALSE, "sss",
                true, "hello",
                Boolean.FALSE, "sss",
                true, "hello",
                Boolean.FALSE, "sss",
                true, "hello",
                Boolean.FALSE, "sss",
                true, "hello",
                Boolean.FALSE, "sss",
                true, "hello",
                Boolean.FALSE, "sss",
        };
        toConditions(objects);
    }

    private static void toConditions(Object... objects) {
        Assert.assertNotNull("条件数组及数组元素不能为空!", objects);
        Assert.assertTrue("数量必须为偶数", objects.length % 2 == 0);

        List<Condition> conditionList = new ArrayList<Condition>();
        for (int i = 0; i < objects.length / 2; i++) {
            final Object evenObject = objects[i * 2];
            final Object oddObject = objects[i * 2 + 1];
            Assert.assertTrue("数组奇数位必须为Boolean类型数值,错误序号 " + (i * 2 + 1), evenObject instanceof Boolean);
            Assert.assertTrue("数组偶数位必须为String 类型数值,错误序号 " + (i * 2 + 2), oddObject instanceof String);

            Boolean expression = (Boolean) evenObject;
            String message = (String) oddObject;
            conditionList.add(new Condition(expression, message));
        }

        ConditionBuilder conditionBuilder = new ConditionBuilder();
        conditionBuilder.and();
    }


    /*private static void toConditions(Object[]... objects) {
        Assert.assertNotNull("条件数组及数组元素不能为空!", objects);
        Integer index = 1;
        List<Condition> conditionList = new ArrayList<Condition>();
        for (Object[] innerObjects : objects) {
            Assert.assertNotNull("条件中每个数组都不能为空!", innerObjects);
            Assert.assertTrue("条件中每个数组都必须有2个元素!", innerObjects.length == 2);
            Assert.assertTrue("条件中每个数组第一个元素必须为Boolean类型数值!错误序号" + index, innerObjects[0] instanceof Boolean);
            Assert.assertTrue("条件中每个数组第二个元素必须为String类型数值!错误序号" + index, innerObjects[1] instanceof String);


            Boolean expression = (Boolean) innerObjects[0];
            String message = (String) innerObjects[1] ;
            conditionList.add(new Condition(expression, message));
            index++;
        }
        ConditionBuilder conditionBuilder = new ConditionBuilder();
        conditionBuilder.and();
    }*/

}
