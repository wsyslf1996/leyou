package com.leyouxianggou.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyouxianggou.common.utils.JsonUtils;
import com.leyouxianggou.item.*;
import com.leyouxianggou.search.client.BrandClient;
import com.leyouxianggou.search.client.CategoryClient;
import com.leyouxianggou.search.client.GoodsClient;
import com.leyouxianggou.search.client.SpecificationClient;
import com.leyouxianggou.search.pojo.Goods;
import com.leyouxianggou.search.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Override
    public Goods buildGoods(Spu spu) {
        Long spuId = spu.getId();

        // 查询分类
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        String categoryNames = categories.stream().map(Category::getName).collect(Collectors.joining(" "));

        // 查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());

        // 构建Goods对象
        Goods goods = new Goods();
        goods.setId(spuId);
        goods.setAll(spu.getTitle() + categoryNames + brand.getName());

        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());

        List<Sku> skus = goodsClient.querySkuListBySpuID(spuId);

        // 添加价格信息和Sku信息
        List<Map<String,Object>> skuList = new ArrayList<>();  // 过滤掉部分不需要的数据
        List<Long> priceList = new ArrayList<>();

        for (Sku sku : skus) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("price",sku.getPrice());
            map.put("images", StringUtils.substringBefore(sku.getImages(),","));
            skuList.add(map);
            // 添加价格
            priceList.add(sku.getPrice());
        }
        goods.setPrice(priceList);  // 价格的集合
        goods.setSkus(JsonUtils.serialize(skuList));  // sku信息的json结构

        //查询规格参数
        List<SpecParam> params = specificationClient.querySpecParamList(null, spu.getCid3(), true);

        //查询商品详情
        SpuDetail spuDetail = goodsClient.queryDetailById(spuId);
        // 查询通用参数
        Map<Long, String> genericSpec = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
        // 查询特有参数
        Map<Long, List> specialSpec = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List>>() {
        });
        Map<String,Object> specs = new HashMap<>();
        for (SpecParam param : params) {
            Object value = null;
            if(param.getGeneric()){
                value = genericSpec.get(param.getId());
            }else {
                value = specialSpec.get(param.getId());
            }
            // 将有分段搜索的属性划分分段
            if(StringUtils.isNotBlank(param.getSegments())){
                value = chooseSegment(value.toString(),param);
            }
            specs.put(param.getName(),value);
        }
        goods.setSpecs(specs);
        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

}
