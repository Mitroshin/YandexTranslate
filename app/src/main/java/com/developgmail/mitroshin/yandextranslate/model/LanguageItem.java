package com.developgmail.mitroshin.yandextranslate.model;

public class LanguageItem {
    private String code;
    private String name;

    public LanguageItem(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Code = " + getCode() + " : Name = " + getName();
    }
}
