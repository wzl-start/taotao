package com.taotao.service;

import com.taotao.pojo.*;

import java.util.Date;
import java.util.List;

public interface ItemService {
    TbItem findItemById(Long itemId);

    LayuiResult findTbItemByPage(int page,int limit);

    TaotaoResult updateItem(List<TbItem> tbItem, int type, Date date);

    LayuiResult itemFuzzyQuery(Integer page, Integer limit, String title, Integer priceMin, Integer priceMax,Long cId);

    TaotaoResult addItem(TbItem tbItem,String itemDesc,String[] paramKeyIds,String[] paramValue);

    PictureResult addPicture(String name, byte[] bytes);

    TaotaoResult showItemGroup(Long cId);
}
