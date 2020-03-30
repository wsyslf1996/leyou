package com.leyouxianggou.service.impl;

import com.leyouxianggou.client.BrandClient;
import com.leyouxianggou.client.CategoryClient;
import com.leyouxianggou.client.GoodsClient;
import com.leyouxianggou.client.SpecificationClient;
import com.leyouxianggou.config.StaticPageConfigurationProperties;
import com.leyouxianggou.item.*;
import com.leyouxianggou.service.StaticPageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@EnableConfigurationProperties(StaticPageConfigurationProperties.class)
public class StaticPageServiceImpl implements StaticPageService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private StaticPageConfigurationProperties staticPageConfigurationProperties;

    @Override
    public Map<String, Object> goodsDetailPage(Long spuId) {
        return loadModel(spuId);
    }

    /**
     * 创建商品详情静态页
     * @param spuId
     */
    @Override
    public void createHtml(Long spuId){
        Map<String, Object> data = loadModel(spuId);
        createHtml(data);
    }

    @Override
    public void createHtml(Map<String, Object> data) {
        Context context = new Context();
        context.setVariables(data);
        Long spuId = ((Spu) data.get("spu")).getId();
        File dest = new File(staticPageConfigurationProperties.getPath(),spuId+".html");
        // 删除已存在的html
        if(dest.exists()){
            dest.delete();
        }
        // 创建新的html
        Writer writer = null;
        try {
            writer = new PrintWriter(dest,"UTF-8");
            templateEngine.process("item",context, writer);
        }catch (Exception e){
            log.error("创建"+spuId+".html失败");
        }finally {
            // 关闭资源
            if(writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("创建商品详情静态页成功，路径："+dest.getAbsolutePath());
    }

    @Override
    public void deleteHtml(long spuId) {
        File file = new File(staticPageConfigurationProperties.getPath(),spuId+".html");
        if(file.exists()){
            file.delete();
            log.info("删除商品详情页，商品SpuID："+spuId);
        }
    }

    /**
     *
     * @param spuId
     * @return
     */
    private Map<String, Object> loadModel(Long spuId){
        Map<String,Object> map = new HashMap<>();
        //准备数据
        Spu spu = goodsClient.querySpuById(spuId);

        Brand brand = brandClient.queryBrandById(spu.getBrandId());

        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));

        List<SpecGroup> specs = specificationClient.queryGroupAndParams(spu.getCid3());

        //数据放入
        map.put("spu",spu);
        map.put("detail",spu.getSpuDetail());
        map.put("categories",categories);
        map.put("brand",brand);
        map.put("skus",spu.getSkus());
        map.put("specs",specs);
        return map;
    }

}
