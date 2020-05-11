package com.taotao.mapper;

import com.taotao.pojo.ItemGroup;
import com.taotao.pojo.ItemGroupKeys;
import com.taotao.pojo.ItemParamGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbItemGroupMapper {
    List<ItemGroup> findGroupByCId(@Param("cId") Long cId);

    List<ItemGroupKeys> findKeysByGroupId(@Param("id") int id);

    int addParamGroup(@Param("groupName")String groupName,@Param("cId")Long cId);

    ItemParamGroup findParamGroupId(@Param("groupName")String groupName,@Param("cId")Long cId);

    int addParamKey(@Param("key")String key, @Param("id")int id);
}
