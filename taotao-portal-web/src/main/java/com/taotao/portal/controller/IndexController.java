package com.taotao.portal.controller;

import com.taotao.pojo.ItemCatResult;
import com.taotao.service.ItemCatService;
import com.taotao.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("/index")
    public String showIndex(){
        return "index";
    }

    @RequestMapping("/itemCat/all")
    @ResponseBody
    public String showItemCat(){
        ItemCatResult itemCatResult = itemCatService.showItemCat();
        String s = JsonUtils.objectToJson(itemCatResult);
        return s;
    }

}
