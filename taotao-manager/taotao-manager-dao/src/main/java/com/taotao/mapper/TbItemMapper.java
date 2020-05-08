package com.taotao.mapper;


import com.taotao.pojo.SearchItem;
import com.taotao.pojo.TbItem;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface TbItemMapper {
    TbItem findTtemById(Long itemId);

    int findTbItemByCount();

    List<TbItem> findTbItemByPage(@Param("index")int index, @Param("pageSize")int pageSize);

    int updateItemByIds(@Param("ids")List<Long> ids, @Param("type")int type,@Param("date")Date date);

    List<TbItem> findItemFuzzyQuery(@Param("page")Integer page, @Param("limit")Integer limit, @Param("title")String title, @Param("priceMin")Integer priceMin, @Param("priceMax")Integer priceMax,@Param("cId")Long cId);

    int findFuzzyQueryCount(@Param("title")String title, @Param("priceMin")Integer priceMin, @Param("priceMax")Integer priceMax, @Param("cId")Long cId);

    int addItemBasicMsg(TbItem tbItem);

    List<SearchItem> findSearchItemAll();

    SearchItem findSearchItemById(@Param("id") Long id);
}