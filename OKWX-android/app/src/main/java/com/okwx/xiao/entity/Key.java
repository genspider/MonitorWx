package com.okwx.xiao.entity;

/***
 * 好友加密key实体类
 */
public class Key {
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Key(String name) {
        this.name = name;
    }

    public Key() {
    }
}
