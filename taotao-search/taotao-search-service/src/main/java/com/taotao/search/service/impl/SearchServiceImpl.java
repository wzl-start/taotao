package com.taotao.search.service.impl;

import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.SearchItem;
import com.taotao.pojo.SearchResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private SolrServer solrServer;

    @Override
    public TaotaoResult importSolr() {
        List<SearchItem> searchItemAll = tbItemMapper.findSearchItemAll();

            try {
                for (SearchItem searchItem:searchItemAll) {
                    SolrInputDocument document = new SolrInputDocument();
                    document.addField("id", searchItem.getId());
                    document.addField("item_title", searchItem.getTitle());
                    document.addField("item_sell_point", searchItem.getSellPoint());
                    document.addField("item_price", searchItem.getPrice());
                    document.addField("item_image", searchItem.getImage());
                    document.addField("item_category_name", searchItem.getCategoryName());
                    document.addField("item_desc", searchItem.getItemDesc());
                    solrServer.add(document);
                }
                solrServer.commit();
                return TaotaoResult.build(200,"导入成功");
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return TaotaoResult.build(500,"导入失败");
    }

    @Override
    public SearchResult findItemSearch(String query, Integer page) {
        SearchResult result = new SearchResult();
        try {
            SolrQuery solrQuery = new SolrQuery();
            solrQuery.setQuery(query);
            solrQuery.set("df","item_keywords");
            //设置高亮
            solrQuery.setHighlight(true);
            solrQuery.addHighlightField("item_title");
            solrQuery.setHighlightSimplePre("<font style='color:red'>");
            solrQuery.setHighlightSimplePost("</font>");
            solrQuery.setStart((page-1)*60);
            solrQuery.setRows(60);
            //service写完


            //dao代码
            QueryResponse queryResponse = solrServer.query(solrQuery);
            SolrDocumentList documentList = queryResponse.getResults();
            long totalConut = documentList.getNumFound();
            long totalPages = (totalConut%60) ==0?(totalConut/60):(totalConut/60)+1;
            //设置总记录条数
            result.setTotalCount(totalConut);
            //设置总页数
            result.setTotalPages(totalPages);

            List<SearchItem> itemList = new ArrayList<SearchItem>();
            for (SolrDocument solrDocument:documentList) {
                SearchItem item = new SearchItem();
                item.setId((String) solrDocument.get("id"));
                item.setCategoryName((String) solrDocument.get("item_category_name"));
                //你取出来的图片也是多张图片
                String item_image = (String) solrDocument.get("item_image");
                item.setImage(item_image);
                item.setPrice((Long) solrDocument.get("item_price"));
                item.setSellPoint((String) solrDocument.get("item_sell_point"));
                String item_title = "";
                Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
                List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
                if(list != null && list.size() > 0){
                    item_title = list.get(0);
                }else{
                    item_title = (String) solrDocument.get("item_title");
                }
                item.setTitle(item_title);
                //吧数据添加到集合里面去
                itemList.add(item);
            }
            result.setItemList(itemList);
            return result;
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addSearchItem(SearchItem searchItem) {
        try {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", searchItem.getId());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSellPoint());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategoryName());
            document.addField("item_desc", searchItem.getItemDesc());
            solrServer.add(document);
            solrServer.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
