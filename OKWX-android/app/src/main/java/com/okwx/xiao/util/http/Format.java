package com.okwx.xiao.util.http;

import java.util.List;

/***
 * json 解析类
 */
public class Format
{
    private String msg;
    private Integer code;
    private List data;

    public Format(String msg, Integer code, List data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public Format() {
    }

    public String getMsg() {
        return msg;
    }

    public Integer getCode() {
        return code;
    }

    public List getData() {
        return data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setData(List data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Format{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
