package com.jonly.frame.dao;

public class Op extends Logic {
    private static final String UNDEFINED = "null";

    private String field;
    private String operand;
    private Object value;

    public Op(String field, String operand, Object value) {
        this.field = field;
        this.operand = operand;
        this.value = value;
    }

    public static Op isNull(String field) {
        return new Op(field, "IS NULL", UNDEFINED);
    }

    public static Op eq(String field, Object value) {
        return new Op(field, "=", value);
    }

    public static Op noteq(String field, Object value) {
        return new Op(field, "!=", value);
    }

    public static Op gt(String field, Object value) {
        return new Op(field, ">", value);
    }

    public static Op gteq(String field, Object value) {
        return new Op(field, ">=", value);
    }

    public static Op lt(String field, Object value) {
        return new Op(field, "<", value);
    }

    public static Op lteq(String field, Object value) {
        return new Op(field, "<=", value);
    }

    public static Op like(String field, Object value) {
        return new Op(field, "LIKE", value);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(this.field).append(this.operand);
        if (!UNDEFINED.equals(this.value)) {
            str.append("?");
        }
        return str.toString();
    }

    @Override
    public Object[] values() {
        if (!UNDEFINED.equals(this.value)) {
            return new Object[] { this.value };
        }
        return null;
    }

}
