package com.taotao.pojo;

import java.io.Serializable;
import java.util.List;

public class ItemGroupResult implements Serializable{
    private int status;
    private String msg;
    private List<ItemGroup> data;

    @Override
    public String toString() {
        return "ItemGroupResult{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ItemGroup> getData() {
        return data;
    }

    public void setData(List<ItemGroup> data) {
        this.data = data;
    }
}
