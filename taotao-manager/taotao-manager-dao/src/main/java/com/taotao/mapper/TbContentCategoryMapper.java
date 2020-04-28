package com.taotao.mapper;


import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbContentCategoryMapper {

    List<TbContentCategory> showContentZtree(@Param("id") Long id);

}