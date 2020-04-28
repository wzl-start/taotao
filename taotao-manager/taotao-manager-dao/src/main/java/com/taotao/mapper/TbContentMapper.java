package com.taotao.mapper;


import com.taotao.pojo.TbContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbContentMapper {

    List<TbContent> findContentByCategoryId(@Param("categoryId")Long categoryId, @Param("index")int index, @Param("limit")Integer limit);

    int findContentCount(Long categoryId);

    int deleteContentByCategoryId(@Param("tbContents") List<TbContent> tbContents);

    void addContent(TbContent tbContent);
}