package com.taotao.pojo;

import java.util.List;

public class PictureResult {
    private int code;
    private String msg;
    private PictureData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PictureData getData() {
        return data;
    }

    public void setData(PictureData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PictureResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
