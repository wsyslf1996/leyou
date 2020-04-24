package com.leyouxianggou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyouxianggou.common.enums.ExceptionEnum;
import com.leyouxianggou.common.enums.LYMQRoutingKey;
import com.leyouxianggou.common.exception.LyException;
import com.leyouxianggou.common.vo.PageResult;
import com.leyouxianggou.item.*;
import com.leyouxianggou.item.mapper.SkuMapper;
import com.leyouxianggou.item.mapper.SpuDetailMapper;
import com.leyouxianggou.item.mapper.SpuMapper;
import com.leyouxianggou.item.mapper.StockMapper;
import com.leyouxianggou.item.service.BrandService;
import com.leyouxianggou.item.service.CategoryService;
import com.leyouxianggou.item.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public PageResult<Spu> querySpuByPage(String key, Boolean saleable, int page, int pageSize) {
        // 分页
        PageHelper.startPage(page,pageSize);

        // 过滤
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //关键字过滤
        if(StringUtils.isNotBlank(key)){
             criteria.andLike("title","%"+key+"%");
        }

        // 是否上架过滤
        if(saleable!=null){
            criteria.andEqualTo("saleable",saleable);
        }

        //排序
        example.setOrderByClause("last_update_time DESC");

        // 查询
        List<Spu> list = spuMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.GOODS_SPU_NOT_FOUND);
        }

        // 解析cid1,cid2,cid3,brand_id这些字段
        list.forEach(spu -> {
            // 解析cid1,cid2,cid3 这几个字段的值，设置cname值为 手机/手机/手机 这种格式
            List<String> names = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names,"/"));

            // 解析brand_id这个字段的值
            spu.setBname(brandService.queryById(spu.getBrandId()).getName());
        });

        // 封装页面信息
        PageInfo<Spu> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getTotal(),list);
    }

    @Override
    public Spu querySpuById(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        if(spu == null){
            throw new LyException(ExceptionEnum.GOODS_SPU_NOT_FOUND);
        }
        spu.setSpuDetail(queryDetailById(spuId));
        spu.setSkus(querySkuListBySpuID(spuId));
        return spu;
    }

    @Override
    @Transactional
    public void insertGoods(Spu spu) {
        //新增Spu
        spu.setId(null);
        spu.setSaleable(true);
        spu.setValid(false);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(new Date());
        int count = spuMapper.insert(spu);
        if(count!=1){
            log.error("新增Spu表信息失败");
            throw new LyException(ExceptionEnum.GOODS_INSERT_ERROR);
        }

        //新增SpuDetail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        count = spuDetailMapper.insert(spuDetail);
        if(count!=1){
            log.error("新增SpuDetail表信息失败");
            throw new LyException(ExceptionEnum.GOODS_INSERT_ERROR);
        }
        insertStockAndSkuBySpu(spu);
        amqpTemplate.convertAndSend(LYMQRoutingKey.ITEM_INSERT,spu.getId());
    }

    @Override
    @Transactional
    public void updateGoods(Spu spu) {
        // 拟解决：当spu中没有sku时，即没有实际商品而只有上架信息时，删除商品SPU信息否？
        if(CollectionUtils.isEmpty(spu.getSkus())){

        }

        // 删除skus和stock信息（重建）
        deleteStockAndSkuBySpu(spu);

        // 插入stock和skus
        insertStockAndSkuBySpu(spu);

        //更新Spu
        spu.setCreateTime(null);
        spu.setLastUpdateTime(new Date());
        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if(count!=1){
            throw new LyException(ExceptionEnum.GOODS_SPU_UPDATE_ERROR);
        }

        //更新SpuDetail
        count = spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if(count!=1){
            throw new LyException(ExceptionEnum.GOODS_SPU_DETAIL_UPDATE_ERROR);
        }

        // 给mq发送消息
        amqpTemplate.convertAndSend(LYMQRoutingKey.ITEM_UPDATE,spu.getId());
    }

    @Override
    public void deleteGoods(Long spuId) {
        //删除stock和sku
        Spu spu = new Spu();
        spu.setId(spuId);
        deleteStockAndSkuBySpu(spu);

        //删除SpuDetail
        spuDetailMapper.deleteByPrimaryKey(spuId);

        //删除Spu
        spuMapper.deleteByPrimaryKey(spuId);

        // 向mq发送消息
        amqpTemplate.convertAndSend(LYMQRoutingKey.ITEM_DELETE,spuId);
    }

    @Override
    public SpuDetail queryDetailById(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        if(spuDetail == null){
            throw new LyException(ExceptionEnum.GOODS_DETAIL_NOT_FOUND);
        }
        return spuDetail;
    }

    @Override
    public List<Sku> querySkuListBySpuID(Long spuId) {
        // 查询Sku
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        if(CollectionUtils.isEmpty(skuList)){
            throw new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        // 查询库存
        setSkusStock(skuList);
        return skuList;
    }

    @Override
    public List<Sku> querySkuListByIds(List<Long> ids) {
        // 查询SkuList
        List<Sku> skuList = skuMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(skuList)){
            throw new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        // 查询并设置每个Sku库存
        setSkusStock(skuList);
        return skuList;
    }

    private void setSkusStock(List<Sku> skus){
        for(Sku sku:skus){
            Stock stock = stockMapper.selectByPrimaryKey(sku.getId());
            if(stock == null){
                throw new LyException(ExceptionEnum.GOODS_STOCK_NOT_FOUND);
            }
            sku.setStock(stock.getStock());
        }
    }

    private void insertStockAndSkuBySpu(Spu spu){
        //新增skus
        List<Sku> skus = spu.getSkus();
        Stock stock = new Stock();
        for (Sku sku : skus) {
            sku.setId(null);
            sku.setSpuId(spu.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(new Date());
            int count = skuMapper.insert(sku);
            if(count!=1){
                log.error("新增Sku表信息失败");
                throw new LyException(ExceptionEnum.GOODS_INSERT_ERROR);
            }

            //新增库存
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            count = stockMapper.insert(stock);
            if(count!=1){
                log.error("新增Stock表信息失败");
                throw new LyException(ExceptionEnum.GOODS_INSERT_ERROR);
            }
        }
    }

    private void deleteStockAndSkuBySpu(Spu spu){
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skuList = skuMapper.select(sku);
        for (Sku sku1 : skuList) {
            //删除库存
            stockMapper.deleteByPrimaryKey(sku1.getId());
            //删除sku
            skuMapper.deleteByPrimaryKey(sku1.getId());
        }
    }

    /**
     * 商品上架
     * @param spuId
     */
    @Override
    public void onShelves(Long spuId) {
        Spu spu = new Spu();
        spu.setId(spuId);
        spu.setSaleable(true);
        spu.setLastUpdateTime(new Date());
        spuMapper.updateByPrimaryKeySelective(spu);

        // 向mq 发送消息
        amqpTemplate.convertAndSend(LYMQRoutingKey.ITEM_ON_SHELVES,spuId);
    }

    /**
     * 商品下架
     * @param spuId
     */
    @Override
    public void offShelves(Long spuId) {
        Spu spu = new Spu();
        spu.setId(spuId);
        spu.setSaleable(false);
        spu.setLastUpdateTime(new Date());
        spuMapper.updateByPrimaryKeySelective(spu);

        // 向mq 发送消息
        amqpTemplate.convertAndSend(LYMQRoutingKey.ITEM_OFF_SHELVES,spuId);
    }

    /**
     * 减少库存
     * @param carts
     */
    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> carts) {
        for (CartDTO cart : carts) {
            System.out.println("执行减库存");
            int count = stockMapper.decreaseStock(cart.getSkuId(), cart.getNum());
            if(count!=1){
                log.error("商品:"+cart.getSkuId()+"库存不足！");
                throw new LyException(ExceptionEnum.GOODS_STOCK_NOT_ENOUGH);
            }
        }
    }
}
