package com.taotao.content.service;

import com.taotao.pojo.*;

import java.util.List;

public interface ItemContentService {
    List<ZtreeResult> showContentZtree(Long id);

    LayuiResult findContentByCategoryId(Long categoryId, Integer page, Integer limit);

    LayuiResult deleteContentByCategoryId(List<TbContent> tbContents, Integer page, Integer limit);

    TaotaoResult addContent(TbContent tbContent);

    List<Ad1Node> showAd1Node();
}
