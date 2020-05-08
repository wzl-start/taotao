package com.taotao.search.service;

import com.taotao.pojo.SearchItem;
import com.taotao.pojo.SearchResult;
import com.taotao.pojo.TaotaoResult;

public interface SearchService {
    TaotaoResult importSolr();

    SearchResult findItemSearch(String str, Integer page);

    void addSearchItem(SearchItem searchItem);
}
