package com.taotao.pojo;

import java.io.Serializable;
import java.util.List;

public class ItemGroup implements Serializable{
    private Integer id;
    private String groupName;
    private Long itemCatId;
    private List<ItemGroupKeys> paramKeys;

    @Override
    public String toString() {
        return "ItemGroup{" +
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

    public List<ItemGroupKeys> getParamKeys() {
        return paramKeys;
    }

    public void setParamKeys(List<ItemGroupKeys> paramKeys) {
        this.paramKeys = paramKeys;
    }
}
