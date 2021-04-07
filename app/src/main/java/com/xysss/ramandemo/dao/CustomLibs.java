package com.xysss.ramandemo.dao;

public class CustomLibs {
    private int code;
    private String name;
    private String saveUrl;
    public CustomLibs() {
    }

    public CustomLibs(int code, String name, String saveUrl) {
        this.code = code;
        this.name = name;
        this.saveUrl=saveUrl;
    }

    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return "CustomLibs{" +
                "code=" + code +
                ", name='" + name + '\'' +
                '}';
    }
}
