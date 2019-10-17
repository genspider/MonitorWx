package com.okwx.xiao.entity;


/***
 * 用户实体类
 */
public class User {
    private String wxNo;
    private String cellphone;
    private Integer fansSum;

    public User() {
    }

    public User(String wxNo, String cellphone, Integer fansSum) {
        this.wxNo = wxNo;
        this.cellphone = cellphone;
        this.fansSum = fansSum;
    }

    public String getWxNo() {
        return wxNo;
    }

    public void setWxNo(String wxNo) {
        this.wxNo = wxNo;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Integer getFansSum() {
        return fansSum;
    }

    public void setFansSum(Integer fansSum) {
        this.fansSum = fansSum;
    }

    @Override
    public String toString() {
        return "User{" +
                "wxNo='" + wxNo + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", fansSum=" + fansSum +
                '}';
    }

}
