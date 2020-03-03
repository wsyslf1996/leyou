package com.leyouxianggou.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyouxianggou.common.utils.JsonUtils;
import com.leyouxianggou.common.vo.PageResult;
import com.leyouxianggou.item.*;
import com.leyouxianggou.search.client.BrandClient;
import com.leyouxianggou.search.client.CategoryClient;
import com.leyouxianggou.search.client.GoodsClient;
import com.leyouxianggou.search.client.SpecificationClient;
import com.leyouxianggou.search.pojo.Goods;
import com.leyouxianggou.search.pojo.SearchRequest;
import com.leyouxianggou.search.pojo.SearchResult;
import com.leyouxianggou.search.repository.GoodsRepository;
import com.leyouxianggou.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

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
        List<Map<String, Object>> skuList = new ArrayList<>();  // 过滤掉部分不需要的数据
        List<Long> priceList = new ArrayList<>();

        for (Sku sku : skus) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            map.put("images", StringUtils.substringBefore(sku.getImages(), ","));
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
        Map<String, Object> specs = new HashMap<>();
        for (SpecParam param : params) {
            Object value = null;
            if (param.getGeneric()) {
                value = genericSpec.get(param.getId());
            } else {
                value = specialSpec.get(param.getId());
            }
            // 将有分段搜索的属性划分分段
            if (StringUtils.isNotBlank(param.getSegments())) {
                value = chooseSegment(value.toString(), param);
            }
            specs.put(param.getName(), value);
        }
        goods.setSpecs(specs);
        return goods;
    }

    @Override
    public SearchResult searchGoods(SearchRequest searchRequest) {
        String key = searchRequest.getKey();
        Integer page = searchRequest.getPage() - 1; // ES中页码是从0开始的
        Integer size = searchRequest.getSize();

        // 1.创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 2.设置分页查询
        queryBuilder.withPageable(PageRequest.of(page, size));
        // 3.设置关键字查询
        QueryBuilder basicQueryBuilder = buildBasicQueryBuilder(searchRequest);
        queryBuilder.withQuery(basicQueryBuilder);

        // 4.过滤不需要的字段(保留需要的字段)
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));

        // 5.设置分类聚合和品牌聚合
        String categoryAggName = "category_agg";
        String brandAggName = "brand_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        // 6.查询返回结果
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);

        // 6.1 解析分类信息
        Aggregations aggs = result.getAggregations();
        List<Category> categories = parseCategory(aggs.get(categoryAggName));
        // 6.2 解析品牌信息
        List<Brand> brands = parseBrand(aggs.get(brandAggName));

        // 7 查询其他过滤参数
        List<Map<String, Object>> specs = null;
        if (!CollectionUtils.isEmpty(categories)) {
            specs = buildSpecs(categories.get(0).getId(), basicQueryBuilder);
        }

        long total = result.getTotalElements();
        long totalPage = result.getTotalPages();
        List<Goods> items = result.getContent();
        return new SearchResult(total, totalPage, items, categories, brands, specs);
    }

    /**
     * 新增或修改索引库中的单条记录
     * @param spuId
     */
    @Override
    public void insertOrUpdateGoodsIndex(long spuId) {
        Spu spu = goodsClient.querySpuById(spuId);
        Goods goods = buildGoods(spu);
        goodsRepository.save(goods);
        log.info("新增或修改Elasticsearch单个商品记录成功：SpuID：" + spuId);
    }

    @Override
    public void deleteGoodsIndex(long spuId) {
        goodsRepository.deleteById(spuId);
        log.info("删除Elasticsearch单个商品记录成功：SpuID："+spuId);
    }

    private QueryBuilder buildBasicQueryBuilder(SearchRequest searchRequest) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 查询条件
        String key = searchRequest.getKey();
        queryBuilder.must(QueryBuilders.termQuery("all", key));
        // 过滤条件
        Map<String, String> filter = searchRequest.getFilter();
        if (filter != null) {
            for (Map.Entry<String, String> entry : filter.entrySet()) {
                String filterName = entry.getKey();
                String filterValue = entry.getValue();
                if(filterName.equals("brandId")|| filterName.equals("cid3")){
                    queryBuilder.filter(QueryBuilders.termQuery(filterName, filterValue));
                }else {
                    queryBuilder.filter(QueryBuilders.termQuery("specs." + filterName + ".keyword", filterValue));
                }
            }
        }
        return queryBuilder;
    }

    private List<Map<String, Object>> buildSpecs(Long cid, QueryBuilder basicQueryBuilder) {
        List<SpecParam> specParams = specificationClient.querySpecParamList(null, cid, true);
        List<Map<String, Object>> specs = new ArrayList<>();

        // 设置参数聚合
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(basicQueryBuilder);
        specParams.forEach(specParam -> {
            String name = specParam.getName();
            queryBuilder.addAggregation(AggregationBuilders.terms(name + "_agg").field("specs." + name + ".keyword"));
        });
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
        Aggregations aggs = result.getAggregations();
        // 解析聚合结果，封装参数
        specParams.forEach(specParam -> {
            String name = specParam.getName();

            StringTerms agg = aggs.get(name + "_agg");
            List<String> specsList = agg.getBuckets().stream().
                    map(StringTerms.Bucket::getKeyAsString).collect(Collectors.toList());
            Map<String, Object> map = new HashMap<>();
            map.put("k", name);
            map.put("option", specsList);
            specs.add(map);
        });
        return specs;
    }

    /**
     * segment分段操作
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    private List<Category> parseCategory(LongTerms aggregation) {
        List<Long> ids = aggregation.getBuckets().stream().
                map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
        return categoryClient.queryCategoryByIds(ids);
    }

    private List<Brand> parseBrand(LongTerms aggregation) {
        List<Long> ids = aggregation.getBuckets().stream().
                map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
        return brandClient.queryBrandByIds(ids);
    }

    private List<String> parseSpecs(StringTerms aggregation) {
        return aggregation.getBuckets().stream().
                map(StringTerms.Bucket::getKeyAsString).collect(Collectors.toList());
    }
}
