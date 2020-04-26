package com.taotao.mapper;

import com.taotao.pojo.ItemGroup;
import com.taotao.pojo.ItemGroupKeys;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbItemGroupMapper {
    List<ItemGroup> findGroupByCId(@Param("cId") Long cId);

    List<ItemGroupKeys> findKeysByGroupId(@Param("id") int id);
}
