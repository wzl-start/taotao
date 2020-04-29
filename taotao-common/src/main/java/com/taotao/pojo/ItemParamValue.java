package com.taotao.pojo;

import java.io.Serializable;

public class ItemParamValue implements Serializable{
    private Long itemId;
    private String paramId;
    private String paramValue;

    @Override
    public String toString() {
        return "ItemParamValue{" +
                "itemId=" + itemId +
                ", paramId='" + paramId + '\'' +
                ", paramValue='" + paramValue + '\'' +
                '}';
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
