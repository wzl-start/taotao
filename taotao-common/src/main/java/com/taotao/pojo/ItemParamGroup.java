package com.taotao.pojo;

import java.io.Serializable;

public class ItemParamGroup implements Serializable {
    private int id;
    private String groupName;
    private Long itemCatId;

    @Override
    public String toString() {
        return "ItemParamGroup{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", itemCatId=" + itemCatId +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getItemCatId() {
        return itemCatId;
    }

    public void setItemCatId(Long itemCatId) {
        this.itemCatId = itemCatId;
    }
}
