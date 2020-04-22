package com.taotao.service.impl;

import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.LayuiResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Override
    public TbItem findItemById(Long itemId) {
        TbItem tbItem = tbItemMapper.findTtemById(itemId);
        return tbItem;
    }

    @Override
    public LayuiResult findTbItemByPage(int page,int limit) {
       LayuiResult layuiResult = new LayuiResult();
       layuiResult.setCode(0);
       layuiResult.setMsg("");
       int count = tbItemMapper.findTbItemByCount();
       layuiResult.setCount(count);

       List<TbItem> data = tbItemMapper.findTbItemByPage((page-1)*limit,limit);
       layuiResult.setData(data);
        return layuiResult;
    }

    @Override
    public TaotaoResult updateItem(List<TbItem> tbItem, int type, Date date) {
        return null;
    }

}
