package com.taotao.content.service.impl;

import com.taotao.content.service.ItemContentService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class ItemContentServiceImpl implements ItemContentService{

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;
    @Autowired
    private TbContentMapper tbContentMapper;

    @Override
    public List<ZtreeResult> showContentZtree(Long id) {

        List<TbContentCategory> tbContentCategories = tbContentCategoryMapper.showContentZtree(id);
        List<ZtreeResult> results = new ArrayList<ZtreeResult>();
        for (TbContentCategory tbContentCategory : tbContentCategories) {
            ZtreeResult ztreeResult = new ZtreeResult();
            ztreeResult.setId(tbContentCategory.getId());
            ztreeResult.setIsParent(tbContentCategory.getIsParent());
            ztreeResult.setName(tbContentCategory.getName());
            results.add(ztreeResult);
        }
        return results;
    }

    @Override
    public LayuiResult findContentByCategoryId(Long categoryId, Integer page, Integer limit) {
        LayuiResult result = new LayuiResult();
        List<TbContent> tbContents = tbContentMapper.findContentByCategoryId(categoryId,(page-1)*limit,limit);
        int count = tbContentMapper.findContentCount(categoryId);
        result.setCode(0);
        result.setMsg("");
        result.setCount(count);
        result.setData(tbContents);
        return result;
    }

    @Override
    public LayuiResult deleteContentByCategoryId(List<TbContent> tbContents, Integer page, Integer limit) {
        LayuiResult layuiResult = new LayuiResult();
        layuiResult.setCode(0);
        layuiResult.setCount(0);
        layuiResult.setMsg("");
        layuiResult.setData(null);
        if(tbContents.size()<=0){
            return layuiResult;
        }
        int i = tbContentMapper.deleteContentByCategoryId(tbContents);
        if (i<=0){
            return layuiResult;
        }
        Long categoryId = tbContents.get(0).getCategoryId();
        int count = tbContentMapper.findContentCount(categoryId);
        if (count<=0){
            return layuiResult;
        }
        layuiResult.setCount(count);
        List<TbContent> data = tbContentMapper.findContentByCategoryId(categoryId,(page-1)*limit,limit);
        layuiResult.setData(data);
        return layuiResult;
    }

    @Override
    public TaotaoResult addContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        tbContentMapper.addContent(tbContent);

        return TaotaoResult.build(200,"商品添加成功");
    }

    @Override
    public List<Ad1Node> showAd1Node() {
        List<TbContent> tbContents = tbContentMapper.findContentByCategoryId(89L, 0, 10);
        List<Ad1Node> nodes = new ArrayList<Ad1Node>();
        for (TbContent content : tbContents) {
            Ad1Node node = new Ad1Node();
            node.setSrcB(content.getPic2());
            node.setHeight(240);
            node.setAlt(content.getTitleDesc());
            node.setWidth(670);
            node.setSrc(content.getPic());
            node.setWidthB(550);
            node.setHref(content.getUrl());
            node.setHeightB(240);
            nodes.add(node);
        }
        return nodes;
    }
}
