package com.taotao.controller;

import com.taotao.pojo.LayuiResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/{itemId}")
    @ResponseBody
    public TbItem findTbItem(@PathVariable Long itemId){
        TbItem result = itemService.findItemById(itemId);
        return result;

    }

    @RequestMapping("/showItemPage")
    @ResponseBody
    public LayuiResult showItemPage(Integer page,Integer limit){
        LayuiResult layuiResult = itemService.findTbItemByPage(page,limit);
        return layuiResult;
    }

    @RequestMapping("/itemDelete")
    @ResponseBody
    public TaotaoResult deleteTtemPage(@RequestBody List<TbItem> tbItems){
        Date date = new Date();
        TaotaoResult taotaoResult = itemService.updateItem(tbItems,2,date);
        return taotaoResult;
    }

    @RequestMapping("/commodityUpperShelves")
    @ResponseBody
    public TaotaoResult commodityUpperShelves(@RequestBody List<TbItem> tbItems){
        Date date = new Date();
        TaotaoResult taotaoResult = itemService.updateItem(tbItems,1,date);
        return taotaoResult;
    }

    @RequestMapping("/commodityLowerShelves")
    @ResponseBody
    public TaotaoResult commodityLowerShelves(@RequestBody List<TbItem> tbItems){
        Date date = new Date();
        TaotaoResult taotaoResult = itemService.updateItem(tbItems,0,date);
        return taotaoResult;
    }
}
