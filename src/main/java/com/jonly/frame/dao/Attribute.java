package com.jonly.frame.dao;

public class Attribute<T> {

    private Object object;
    private String property;
    private String field;
    
    public Object getObject() {
        return object;
    }
    public void setObject(Object object) {
        this.object = object;
    }
    public String getProperty() {
        return property;
    }
    public void setProperty(String property) {
        this.property = property;
    }
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    
}

