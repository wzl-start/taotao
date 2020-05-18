package com.taotao.order.service;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import com.taotao.pojo.TbUser;

import java.util.List;

public interface OrderService {
    TaotaoResult addOrder(List<TbOrderItem> orderItems, TbOrderShipping orderShipping, String payment, Integer paymentType, TbUser tbUser);
}
