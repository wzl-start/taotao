package com.taotao.service.impl;

import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.*;
import com.taotao.service.ItemService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public TaotaoResult updateItem(List<TbItem> tbItems, int type, Date date) {
        List<Long> ids = new ArrayList<Long>();
        if (tbItems.size()<=0){
            return TaotaoResult.build(500,"请先勾选在操作",null);
        }
        for (TbItem tbItem:tbItems) {
            ids.add(tbItem.getId());
        }
        int count = tbItemMapper.updateItemByIds(ids,type,date);
        if (count>0&&type==0){
            return TaotaoResult.build(200,"商品下架成功",null);
        }else if (count>0&&type==1){
            return TaotaoResult.build(200,"商品上架成功",null);
        }else if (count>0&&type==2){
            return TaotaoResult.build(200,"商品删除成功",null);
        }
        return TaotaoResult.build(500,"商品修改失败",null);
    }

    @Override
    public LayuiResult itemFuzzyQuery(Integer page, Integer limit, String title, Integer priceMin, Integer priceMax,Long cId) {
        if (priceMin == null){
            priceMin = 0;
        }
        if (priceMax == null){
            priceMax = 1000000;
        }
        LayuiResult layuiResult = new LayuiResult();
        layuiResult.setCode(0);
        layuiResult.setMsg("");
        int count = tbItemMapper.findFuzzyQueryCount(title,priceMin,priceMax,cId);
        layuiResult.setCount(count);

        List<TbItem> data = tbItemMapper.findItemFuzzyQuery(page,limit,title,priceMin,priceMax,cId);
        System.out.println(data);
        layuiResult.setData(data);
        return layuiResult;
    }
}
