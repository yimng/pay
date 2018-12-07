package com.edupay.commons.condition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * com.caicui.commons.condition
 * Created by yukewi on 2015/8/5 17:29.
 */
public class ConditionBuilder {
    private List<Map<Condition, ConditionType>> conditionsMapList = new ArrayList<Map<Condition, ConditionType>>();

    /**
     * 条件与
     *
     * @param conditions
     * @return
     */
    public ConditionBuilder and(Condition... conditions) {
        append(ConditionType.AND, conditions);
        return this;
    }

    /**
     * 条件与
     *
     * @param conditions
     * @return
     */
    public ConditionBuilder and(List<Condition> conditions) {
        append(ConditionType.AND, conditions.toArray(new Condition[conditions.size()]));
        return this;
    }

    /**
     * 追加条件
     *
     * @param conditionType
     * @param condition
     * @return
     */
    public ConditionBuilder append(ConditionType conditionType, Condition... condition) {
        Map<Condition, ConditionType> conditionsMap = new LinkedHashMap<Condition, ConditionType>();
        for (Condition condition_ : condition) {
            conditionsMap.put(condition_, conditionType);
        }
        this.conditionsMapList.add(conditionsMap);
        return this;
    }


    public List<Map<Condition, ConditionType>> getConditionsMapList() {
        return conditionsMapList;
    }

    /**
     * 条件或
     *
     * @param conditions
     * @return
     */
    public ConditionBuilder or(Condition... conditions) {
        append(ConditionType.OR, conditions);
        return this;
    }

    /**
     * 条件或
     *
     * @param conditions
     * @return
     */
    public ConditionBuilder or(List<Condition> conditions) {
        append(ConditionType.OR, conditions.toArray(new Condition[conditions.size()]));
        return this;
    }


    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        Integer index = 1;
        for (Map<Condition, ConditionType> conditionsMap : conditionsMapList) {
            for (Map.Entry<Condition, ConditionType> entry : conditionsMap.entrySet()) {
                final Condition condition = entry.getKey();
                final ConditionType conditionType = entry.getValue();
                if (index != 1) {
                    toStringBuilder.append(ConditionType.toExpression(conditionType));
                }
                toStringBuilder.append(condition);
                index++;
            }
        }
        return toStringBuilder.toString();
    }


    public enum ConditionType {
        AND, OR;

        public static String toExpression(ConditionType conditionType) {
            switch (conditionType) {
                case AND:
                    return "&&";
                case OR:
                    return "||";
                default:
                    return "&&";
            }
        }
    }
}
