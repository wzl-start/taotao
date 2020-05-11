package com.taotao.service.impl;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.taotao.constant.OSSConstant;
import com.taotao.constant.RedisConstant;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemGroupMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.*;
import com.taotao.service.ItemService;
import com.taotao.service.JedisClient;
import com.taotao.utils.IDUtils;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;
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
    @Autowired
    private TbItemParamMapper tbItemParamMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination destination;
    @Autowired
    private JedisClient jedisClient;

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
        layuiResult.setData(data);
        return layuiResult;
    }

    @Override
    public TaotaoResult addItem(TbItem tbItem,String itemDesc,String[] paramKeyIds,String[] paramValue) {
        final Long itemId = IDUtils.genItemId();
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

        List<ItemParamValue> itemParamValues = new ArrayList<ItemParamValue>();
        for (int i = 0; i < paramKeyIds.length; i++) {
            ItemParamValue itemParamValue = new ItemParamValue();
            itemParamValue.setItemId(itemId);
            itemParamValue.setParamId(paramKeyIds[i]);
            itemParamValue.setParamValue(paramValue[i]);
            itemParamValues.add(itemParamValue);
        }

        int count2 = tbItemParamMapper.addParamValue(itemParamValues);
        if (count2<0){
            return TaotaoResult.build(500,"添加商品规格信息失败",null);
        }

        jmsTemplate.send(destination,new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setText(itemId+"");
                return textMessage;
            }
        });

        return TaotaoResult.build(200,"添加商品成功",null);
    }

    @Override
    public PictureResult addPicture(String name, byte[] bytes) {
        OSS ossClient = new OSSClientBuilder().build(OSSConstant.ENDPOINT, OSSConstant.ACCESSKEYID, OSSConstant.ACCESSKEYSECRET);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        //上传图片
        ossClient.putObject(OSSConstant.BUCKETNAME,OSSConstant.OBJECTNAME+name,bis);

        // 指定过期时间
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(OSSConstant.BUCKETNAME, OSSConstant.OBJECTNAME, HttpMethod.GET);
        req.setExpiration(expiration);

        PictureData data = new PictureData();
        data.setSrc("https://taotao-wzl.oss-cn-chengdu.aliyuncs.com/taotao-picture/"+name);
        PictureResult pictureResult = new PictureResult();
        pictureResult.setCode(0);
        pictureResult.setMsg("");
        pictureResult.setData(data);
        // 关闭OSSClient。
        ossClient.shutdown();
        return pictureResult;
    }

    @Override
    public TaotaoResult showItemGroup(Long cId) {
        List<ItemGroup> itemGroups = tbItemGroupMapper.findGroupByCId(cId);
        if (itemGroups == null ||itemGroups.size()==0){
            return TaotaoResult.build(500,"没有规格参数模板请创建");
        }

        for (ItemGroup itemGroup : itemGroups) {
            List<ItemGroupKeys> itemGroupKeys = tbItemGroupMapper.findKeysByGroupId(itemGroup.getId());
            itemGroup.setParamKeys(itemGroupKeys);
        }

        TaotaoResult result = new TaotaoResult();
        result.setStatus(200);
        result.setMsg("有规格参数模板");
        result.setData(itemGroups);
        return result;
    }

    @Override
    public TaotaoResult addItemGroup(Long cId, String params) {

        String[] strings = params.split("clive");
        int s = 0;
        for (int i = 0; i < strings.length; i++) {
            String[] splits = strings[i].split(",");
            String groupName = splits[0];
            s = tbItemGroupMapper.addParamGroup(groupName,cId);
            ItemParamGroup itemParamGroup = tbItemGroupMapper.findParamGroupId(groupName,cId);
            for (int j = 1; j < splits.length; j++) {
                String key = splits[j];
                s = tbItemGroupMapper.addParamKey(key,itemParamGroup.getId());
            }
        }
        if (s == 0){
            return TaotaoResult.build(500,"添加规格参数模板失败");
        }
        return TaotaoResult.build(200,"添加规格参数模板成功");
    }

    @Override
    public TbItem getItemById(Long itemId) {

        String json = jedisClient.get("RedisConstant.ITEM_INFO");
        int rand = (int)(Math.random()*1000)+1;
        //当json不为null 有数据的时候
        if (StringUtils.isNotBlank(json)){
            if (json.equals("null")){
                return null;
            }else {
                TbItem tbItem = JsonUtils.jsonToPojo(json,TbItem.class);
                jedisClient.expire(RedisConstant.ITEM_INFO,RedisConstant.REDIS_TIME_OUT+rand);
                return tbItem;
            }
        }

        TbItem tbItem = tbItemMapper.findItemById(itemId);
        if (tbItem==null){
            jedisClient.set(RedisConstant.ITEM_INFO,"null");
            jedisClient.expire(RedisConstant.ITEM_INFO,RedisConstant.REDIS_TIME_OUT);
        }else {
            //吧查询数据库得到的结果集存入到redis缓存中
            jedisClient.set(RedisConstant.ITEM_INFO, JsonUtils.objectToJson(tbItem));
            jedisClient.expire(RedisConstant.ITEM_INFO,RedisConstant.REDIS_TIME_OUT+rand);
        }

        return tbItem;
    }

    @Override
    public TbItemDesc getItemDescByItemId(Long itemId) {

        String json = jedisClient.get(RedisConstant.ITEM_DESC);
        int rand = (int)(Math.random()*1000)+1;
        //当json不为null 有数据的时候
        if(StringUtils.isNotBlank(json)){
            if(json.equals("null")){
                return null;
            }else{
                TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);

                jedisClient.expire(RedisConstant.ITEM_DESC,RedisConstant.REDIS_TIME_OUT+rand);
                return itemDesc;
            }
        }

        TbItemDesc tbItemDesc = tbItemMapper.findItemDescByItemId(itemId);
        if(tbItemDesc==null){
            jedisClient.set(RedisConstant.ITEM_DESC,"null");
            jedisClient.expire(RedisConstant.ITEM_DESC,RedisConstant.REDIS_TIME_OUT);
        }else {
            //吧查询数据库得到的结果集存入到redis缓存中
            jedisClient.set(RedisConstant.ITEM_DESC, JsonUtils.objectToJson(tbItemDesc));
            jedisClient.expire(RedisConstant.ITEM_DESC,RedisConstant.REDIS_TIME_OUT+rand);
        }
        return tbItemDesc;
    }

    @Override
    public String findTbItemGroupByItemId(Long itemId) {

        String json = jedisClient.get(RedisConstant.ITEM_PARAM);
        int rand = (int)(Math.random()*1000)+1;

        //当json不为null 有数据的时候
        if(StringUtils.isNotBlank(json)){
            if(json.equals("null")){
                return null;
            }else{
                String str = JsonUtils.jsonToPojo(json, String.class);
                jedisClient.expire(RedisConstant.ITEM_PARAM,RedisConstant.REDIS_TIME_OUT+rand);
                return str;
            }
        }

        List<ItemGroup> groups = tbItemMapper.findTbItemGroupByItemId(itemId);
        StringBuffer sb = new StringBuffer();
        if(groups==null){
            jedisClient.set(RedisConstant.ITEM_PARAM,"null");
            jedisClient.expire(RedisConstant.ITEM_PARAM,RedisConstant.REDIS_TIME_OUT);
        }else {
            //吧查询数据库得到的结果集存入到redis缓存中
            sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\">\n");
            sb.append("    <tbody>\n");
            for (ItemGroup group:groups) {
                sb.append("        <tr>\n");
                sb.append("            <th class=\"tdTitle\" colspan=\"2\">"+group.getGroupName()+"</th>\n");
                sb.append("        </tr>\n");
                List<ItemGroupKeys> paramKeys = group.getParamKeys();
                for (ItemGroupKeys paramKey:paramKeys) {
                    sb.append("        <tr>\n");
                    sb.append("            <td class=\"tdTitle\">"+paramKey.getParamName()+"</td>\n");
                    sb.append("            <td>"+paramKey.getItemParamValue().getParamValue()+"</td>\n");
                    sb.append("        </tr>\n");
                }
            }
            sb.append("    </tbody>\n");
            sb.append("</table>");
            jedisClient.set(RedisConstant.ITEM_PARAM, JsonUtils.objectToJson(sb));
            jedisClient.expire(RedisConstant.ITEM_PARAM,RedisConstant.REDIS_TIME_OUT+rand);
        }
        return sb.toString();

    }
}
