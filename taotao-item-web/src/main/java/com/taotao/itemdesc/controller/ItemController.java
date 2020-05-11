package com.taotao.itemdesc.controller;

import com.taotao.pojo.Item;
import com.taotao.pojo.ItemGroup;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/{itemId}")
    public String showItem(@PathVariable Long itemId, Model model){
        TbItem tbItem = itemService.getItemById(itemId);
        Item item = new Item(tbItem);
        for (String string:item.getImages()) {
            System.out.println(string);
        }
        model.addAttribute("item",item);
        return "item";
    }

    @ResponseBody
    @RequestMapping("/desc/{itemId}")
    public String showItemDesc(@PathVariable Long itemId,Model model){
        TbItemDesc tbItemDesc = itemService.getItemDescByItemId(itemId);
        return tbItemDesc.getItemDesc();
    }

    @RequestMapping("/param/{itemId}")
    @ResponseBody
    public String showItemParam(@PathVariable Long itemId){
        String str = itemService.findTbItemGroupByItemId(itemId);
        return str;
    }

}
