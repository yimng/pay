package com.edupay.commons.condition;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * com.caicui.commons.condition
 * Created by yukewi on 2015/8/5 17:21.
 */

public class Condition implements Serializable {
    private static final long serialVersionUID = -2081819587657760569L;
    private Boolean expression;
    private String message;

    public Condition(Boolean expression, String message) {
        this.expression = expression;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    public Boolean getExpression() {
        return expression;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public void setExpression(Boolean expression) {
        this.expression = expression;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "(" + expression + ":" + message + ")";
    }
}
