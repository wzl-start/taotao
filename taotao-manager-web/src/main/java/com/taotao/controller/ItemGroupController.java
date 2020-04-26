package com.taotao.controller;

import com.taotao.pojo.ItemGroupResult;
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
    public ItemGroupResult showItemGroup(Long cId){
        ItemGroupResult itemGroupResult = itemService.showItemGroup(cId);
        return itemGroupResult;
    }


}
