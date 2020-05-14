package com.taotao.cart.controller;

import com.taotao.constant.RedisConstant;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/add/{itemId}")
    public String add(@PathVariable Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        String tokenValue = CookieUtils.getCookieValue(request, RedisConstant.TT_TOKEN);
        List<TbItem> cartList = new ArrayList<TbItem>();
        String json = CookieUtils.getCookieValue(request, RedisConstant.TT_CART, true);
        if (StringUtils.isNotBlank(json)) {
            //把json转换成商品列表返回
            cartList = JsonUtils.jsonToList(json, TbItem.class);
        }
        //判断商品在商品列表中是否存在。
        boolean hasItem = false;
        for (TbItem tbItem : cartList) {
            //对象比较的是地址，应该是值的比较
            if (tbItem.getId() == itemId.longValue()) {
                // 3、如果存在，商品数量相加。
                tbItem.setNum(tbItem.getNum() + num);
                hasItem = true;
                break;
            }
        }

        if (!hasItem) {
            TbItem tbItem = itemService.getItemById(itemId);
            String image = tbItem.getImage();
            if (StringUtils.isNoneBlank(image)) {
                String[] strings = image.split("http");
                String string = "http" + strings[1];
                tbItem.setImage(string);
            }
            //设置购买商品数量
            tbItem.setNum(num);
            cartList.add(tbItem);
            //把购车商品列表写入cookie。
            CookieUtils.setCookie(request, response, RedisConstant.TT_CART, JsonUtils.objectToJson(cartList), RedisConstant.CART_EXPIRE, true);
        }
        return "cartSuccess";
    }

    @RequestMapping(value = "{cart}")
    public String showCartList(HttpServletRequest request, Model model) {
        String json = CookieUtils.getCookieValue(request, RedisConstant.TT_CART, true);
        //传递给页面
        List<TbItem> cartList = JsonUtils.jsonToList(json, TbItem.class);
        int sum = 0;
        for (TbItem tbItem : cartList) {
            sum+=tbItem.getNum();
        }
        model.addAttribute("sum", sum);
        model.addAttribute("cartList", cartList);
        return "cart";
    }

    @RequestMapping("/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {

        String json = CookieUtils.getCookieValue(request, RedisConstant.TT_CART, true);
        //传递给页面
        List<TbItem> cartList = JsonUtils.jsonToList(json, TbItem.class);
        for (int i = 0; i < cartList.size(); i++) {
            TbItem tbItem = cartList.get(i);
            if (tbItem.getId() == itemId.longValue()) {
                cartList.remove(tbItem);
                break;
            }
        }
        //集合里面一定有值了 并且把他存入我们的cookie里面
        CookieUtils.setCookie(request, response, RedisConstant.TT_CART, JsonUtils.objectToJson(cartList), RedisConstant.CART_EXPIRE, true);
        return "redirect:/cart/cart.html";
    }

    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateNum(@PathVariable Long itemId, @PathVariable Integer num,
                                  HttpServletRequest request, HttpServletResponse response) {
        // 1、接收两个参数
        // 2、从cookie中取商品列表
        String json = CookieUtils.getCookieValue(request, RedisConstant.TT_CART, true);
        //传递给页面
        List<TbItem> cartList = JsonUtils.jsonToList(json, TbItem.class);
        // 3、遍历商品列表找到对应商品
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()) {
                // 4、更新商品数量
                tbItem.setNum(num);
            }
        }
        // 5、把商品列表写入cookie。
        CookieUtils.setCookie(request, response,RedisConstant.TT_CART, JsonUtils.objectToJson(cartList),RedisConstant.CART_EXPIRE, true);
        // 6、响应TaoTaoResult。Json数据。
        return TaotaoResult.ok();
    }
}