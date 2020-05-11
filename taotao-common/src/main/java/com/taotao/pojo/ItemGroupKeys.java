package com.taotao.pojo;

import java.io.Serializable;

public class ItemGroupKeys implements Serializable {
    private int id;
    private String paramName;
    private int groupId;
    //private String paramGroup;
    private ItemGroup itemGroup;
    private ItemParamValue itemParamValue;

    @Override
    public String toString() {
        return "ItemGroupKeys{" +
                "id=" + id +
                ", paramName='" + paramName + '\'' +
                ", groupId=" + groupId +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public ItemGroup getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(ItemGroup itemGroup) {
        this.itemGroup = itemGroup;
    }

    public ItemParamValue getItemParamValue() {
        return itemParamValue;
    }

    public void setItemParamValue(ItemParamValue itemParamValue) {
        this.itemParamValue = itemParamValue;
    }
}
