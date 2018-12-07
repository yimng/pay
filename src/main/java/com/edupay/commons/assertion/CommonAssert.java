package com.edupay.commons.assertion;

import com.edupay.commons.condition.Condition;
import com.edupay.commons.condition.ConditionBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * com.caicui.commons.assertion
 * Created by yukewi on 2015/8/6 14:58.
 */
public class CommonAssert extends Assert {

    public static void assertTrue(Condition... conditions) {
        ConditionBuilder conditionBuilder = new ConditionBuilder();
        conditionBuilder.and(conditions);
        assertTrue(conditionBuilder);
    }

    public static void assertTrue(ConditionBuilder conditionBuilder) {
        Boolean finalResult = true;
        Integer index = 1;
        for (Map<Condition, ConditionBuilder.ConditionType> conditionConditionTypeMap : conditionBuilder.getConditionsMapList()) {
            for (Map.Entry<Condition, ConditionBuilder.ConditionType> conditionTypeEntry : conditionConditionTypeMap.entrySet()) {
                final Condition condition = conditionTypeEntry.getKey();
                final ConditionBuilder.ConditionType conditionType = conditionTypeEntry.getValue();
                if (index == 1) {
                    finalResult = condition.getExpression();
                } else if (conditionType.equals(ConditionBuilder.ConditionType.AND)) {
                    finalResult = finalResult && condition.getExpression();
                } else if (conditionType.equals(ConditionBuilder.ConditionType.OR)) {
                    finalResult = finalResult || condition.getExpression();
                }
                if (!finalResult) {
                    throw new RuntimeException(condition.getMessage());
                }
                index++;
            }
        }
    }

    public static void assertTrue(Object... conditions) {
        final ConditionBuilder conditionBuilder = toConditions(conditions);
        assertTrue(conditionBuilder);
        
    }

    public static void assertTrue(Object[]... conditions) {
        final ConditionBuilder conditionBuilder = toConditions(conditions);
        assertTrue(conditionBuilder);
    }

    private static ConditionBuilder toConditions(Object... objects) {
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
        return conditionBuilder;
    }


    private static ConditionBuilder toConditions(Object[]... objects) {
        Assert.assertNotNull("条件数组及数组元素不能为空!", objects);
        Integer index = 1;
        List<Condition> conditionList = new ArrayList<Condition>();
        for (Object[] innerObjects : objects) {
            Assert.assertNotNull("条件中每个数组都不能为空!", innerObjects);
            Assert.assertTrue("条件中每个数组都必须有2个元素!", innerObjects.length == 2);
            Assert.assertTrue("条件中每个数组第一个元素必须为Boolean类型数值!错误序号" + index, innerObjects[0] instanceof Boolean);
            Assert.assertTrue("条件中每个数组第二个元素必须为String类型数值!错误序号" + index, innerObjects[1] instanceof String);


            Boolean expression = (Boolean) innerObjects[0];
            String message = (String) innerObjects[1];
            conditionList.add(new Condition(expression, message));
            index++;
        }
        ConditionBuilder conditionBuilder = new ConditionBuilder();
        conditionBuilder.and();
        return conditionBuilder;
    }
}
