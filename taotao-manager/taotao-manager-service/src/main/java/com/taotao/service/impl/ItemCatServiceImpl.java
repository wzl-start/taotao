package com.taotao.service.impl;


import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.*;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Override
    public List<ZtreeResult> getZtreeResult(Long id) {

        List<TbItemCat> tbItemCats = tbItemCatMapper.findTbItemCatByParentId(id);

        List<ZtreeResult> results = new ArrayList<ZtreeResult>();
        for (TbItemCat tbItemCat :tbItemCats) {
            ZtreeResult result = new ZtreeResult();
            result.setId(tbItemCat.getId());
            result.setName(tbItemCat.getName());
            result.setIsParent(tbItemCat.getIsParent());
            results.add(result);
        }
        return results;
    }

    @Override
    public ItemCatResult showItemCat() {
        ItemCatResult result = new ItemCatResult();
        result.setData(getItemCatList(0L));
        return result;
    }

    private List<?> getItemCatList(Long parentId){
        List list = new ArrayList();
        int count = 0;
        List<TbItemCat> tbItemCats = tbItemCatMapper.findTbItemCatByParentId(parentId);
        for (TbItemCat tbItemCat : tbItemCats) {
            ItemCat itemCat = new ItemCat();
            if (tbItemCat.getIsParent()){
                itemCat.setU("/products/"+tbItemCat.getId()+".html");
                if (tbItemCat.getParentId()==0){
                    itemCat.setN("<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>");
                }else {
                    itemCat.setN(tbItemCat.getName());
                }
                itemCat.setI(getItemCatList(tbItemCat.getId()));
                list.add(itemCat);
                count++;
                if (parentId == 0 && count >= 14){
                    break;
                }
            }else {
                list.add("/products/"+tbItemCat.getId()+".html|"+tbItemCat.getName());
            }
        }
        return list;
    }
}
