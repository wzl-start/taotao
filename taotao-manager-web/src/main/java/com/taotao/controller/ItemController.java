package com.taotao.controller;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.taotao.pojo.*;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
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

    @RequestMapping("/searchItem")
    @ResponseBody
    public LayuiResult searchItem(Integer page,Integer limit,String title,Integer priceMin,Integer priceMax,Long cId){
        LayuiResult result = itemService.itemFuzzyQuery(page,limit,title,priceMin,priceMax,cId);
        System.out.println(result);
        return result;
    }

    @RequestMapping("/fileUpload")
    @ResponseBody
    public PictureResult fileUpload(MultipartFile file) throws IOException {
        String endpoint = "http://oss-cn-chengdu.aliyuncs.com";
        String accessKeyId = "LTAI4G8ZBpxyHHokra2mn8RB";
        String accessKeySecret = "WXJ4Dym4o0zzmK5mPWYl0wLyDW6myY";
        String bucketName = "taotao-wzl";
        String objectName = "taotao-picture/";

        InputStream is = file.getInputStream();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName,objectName+file.getName(),is);
        // 设置图片处理样式。
        //String style = "image/resize,m_fixed,w_100,h_100/rotate,90";
        // 指定过期时间为10分钟。
        Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10 );
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.GET);
        req.setExpiration(expiration);
        //req.setProcess(style);
        String signedUrl = ossClient.generatePresignedUrl(req).toString();
        System.out.println(signedUrl);
        PictureData data = new PictureData();
        data.setSrc(signedUrl);
        PictureResult pictureResult = new PictureResult();
        pictureResult.setCode(0);
        pictureResult.setMsg("");
        pictureResult.setData(data);
        // 关闭OSSClient。
        ossClient.shutdown();
        //file.transferTo(new File("D://",filename));
        return pictureResult;
    }

    @RequestMapping("/addItem")
    @ResponseBody
    public TaotaoResult addItem(Long cId,String title,String sellPoint,Integer price,Integer num,String barcode,String file,String image){
        TaotaoResult result = itemService.addItemBasicMsg(cId,title,sellPoint,price,num,barcode,file,image);
        return result;
    }


}
