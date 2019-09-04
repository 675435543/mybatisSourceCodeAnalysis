package com.atguigu.mybatis.bean;

import java.io.Serializable;

public class NameAndVal implements Serializable {
    private static final long serialVersionUID = 1L;
    private String lastName;
    private String val;

    public NameAndVal() {
    }

    public NameAndVal(String lastName, String val) {
        this.lastName = lastName;
        this.val = val;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
