package com.taotao.controller;

import com.taotao.pojo.TaotaoResult;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/itemGroup")
public class ItemGroupController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/showItemGroup")
    @ResponseBody
    public TaotaoResult showItemGroup(Long cId){
        TaotaoResult result = itemService.showItemGroup(cId);
        return result;
    }

    @RequestMapping("/addGroup")
    @ResponseBody
    public TaotaoResult addGroup(Long cId,String params){
        TaotaoResult result = itemService.addItemGroup(cId,params);
        return result;
    }
}
