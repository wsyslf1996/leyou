package com.leyouxianggou.search;

import com.leyouxianggou.common.vo.PageResult;
import com.leyouxianggou.item.Spu;
import com.leyouxianggou.search.client.GoodsClient;
import com.leyouxianggou.search.pojo.Goods;
import com.leyouxianggou.search.repository.GoodsRepository;
import com.leyouxianggou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SearchDemo {

    @Autowired
    public GoodsClient goodsClient;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private SearchService searchService;

    @Test
    public void insertIndex(){
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }

    @Test
    public void importData(){

        int page = 1;
        int pageSize = 100;
        int size = 0 ;
        List<Goods> goodsList = new ArrayList<>();
        do{
            PageResult<Spu> pageResult = goodsClient.querySpuByPage(null, true, page, pageSize);
            List<Spu> spus = pageResult.getItems();
            size = spus.size();
            for (Spu spu : spus) {
                Goods goods = searchService.buildGoods(spu);
                goodsList.add(goods);
            }
            page++;
        }while (size==100);
        goodsRepository.saveAll(goodsList);
    }

    @Test
    public void deleteIndex(){
        template.deleteIndex(Goods.class);
    }
}
