package com.jonly.frame.dto;

public class SqlParam {

    private Object o;

    public SqlParam(Object o) {
        this.o = o;
    }

    public static SqlParam create(Object o) {
        return new SqlParam(o);
    }

    public Object getO() {
        return o;
    }

    public void setO(Object o) {
        this.o = o;
    }
}

