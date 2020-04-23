package com.taotao.mapper;

import com.taotao.pojo.TbItemCat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbItemCatMapper {

    List<TbItemCat> findTbItemCatByParentId(@Param("id")Long id);
}