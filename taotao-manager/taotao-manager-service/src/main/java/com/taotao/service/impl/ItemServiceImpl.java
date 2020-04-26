package com.taotao.service.impl;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.taotao.constant.OSSConstant;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemGroupMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.*;
import com.taotao.service.ItemService;
import com.taotao.utils.IDUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private TbItemGroupMapper tbItemGroupMapper;

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

    @Override
    public TaotaoResult addItem(TbItem tbItem,String itemDesc) {
        Long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        Date date = new Date();
        tbItem.setCreated(date);
        tbItem.setStatus((byte)1);
        tbItem.setUpdated(date);
        int count = tbItemMapper.addItemBasicMsg(tbItem);
        if (count<0){
            return TaotaoResult.build(500,"添加商品失败",null);
        }
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        tbItemDesc.setItemDesc(itemDesc);
        int count1 = tbItemDescMapper.addItemDesc(tbItemDesc);
        if (count1<0){
            return TaotaoResult.build(500,"添加商品描述信息失败",null);
        }

        return TaotaoResult.build(200,"添加商品成功",null);
    }

    @Override
    public PictureResult addPicture(String name, byte[] bytes) {
        OSS ossClient = new OSSClientBuilder().build(OSSConstant.ENDPOINT, OSSConstant.ACCESSKEYID, OSSConstant.ACCESSKEYSECRET);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        String newName = IDUtils.genImageName() + name.substring(name.lastIndexOf("."));
        //上传图片
        ossClient.putObject(OSSConstant.BUCKETNAME,OSSConstant.OBJECTNAME+newName,bis);

        // 指定过期时间
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(OSSConstant.BUCKETNAME, OSSConstant.OBJECTNAME, HttpMethod.GET);
        req.setExpiration(expiration);

        String signedUrl = ossClient.generatePresignedUrl(req).toString();
        PictureData data = new PictureData();
        data.setSrc(signedUrl);
        PictureResult pictureResult = new PictureResult();
        pictureResult.setCode(0);
        pictureResult.setMsg("");
        pictureResult.setData(data);
        // 关闭OSSClient。
        ossClient.shutdown();
        return pictureResult;
    }

    @Override
    public ItemGroupResult showItemGroup(Long cId) {
        List<ItemGroup> itemGroups = tbItemGroupMapper.findGroupByCId(cId);

        for (ItemGroup itemGroup : itemGroups) {
            List<ItemGroupKeys> itemGroupKeys = tbItemGroupMapper.findKeysByGroupId(itemGroup.getId());
            itemGroup.setParamKeys(itemGroupKeys);
        }

        ItemGroupResult itemGroupResult = new ItemGroupResult();
        itemGroupResult.setStatus(200);
        itemGroupResult.setMsg("有规格参数模板");
        itemGroupResult.setData(itemGroups);
        return itemGroupResult;
    }
}
