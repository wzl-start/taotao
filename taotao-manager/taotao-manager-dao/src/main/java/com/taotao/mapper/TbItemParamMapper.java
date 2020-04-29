package com.taotao.mapper;


import com.taotao.pojo.ItemParamValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbItemParamMapper {

    int addParamValue(@Param("itemParamValues") List<ItemParamValue> itemParamValues);
}