package com.taotao.mapper;


import com.taotao.pojo.TbItem;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface TbItemMapper {
    TbItem findTtemById(Long itemId);

    int findTbItemByCount();

    List<TbItem> findTbItemByPage(@Param("index")int index, @Param("pageSize")int pageSize);

    int updateItemByIds(@Param("ids")List<Long> ids, @Param("type")int type,@Param("date")Date date);
}