package com.taotao.search.listener;

import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.SearchItem;
import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private SearchService searchService;


    @Override
    public void onMessage(Message message) {
        System.out.println("22222");
        TextMessage textMessage = (TextMessage) message;
        try {
            String id = textMessage.getText();
            SearchItem searchItem = tbItemMapper.findSearchItemById(Long.valueOf(id));
            searchService.addSearchItem(searchItem);
            System.out.println(searchItem);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
