package com.leyouxianggou.cart.service.impl;

import com.leyouxianggou.auth.entity.UserInfo;
import com.leyouxianggou.cart.interceptor.UserInterceptor;
import com.leyouxianggou.cart.pojo.CartItem;
import com.leyouxianggou.cart.service.CartService;
import com.leyouxianggou.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private UserInfo user;
    private Long uid;
    private BoundHashOperations<String, Object, Object> cart;

    public static final String CARTREDIS_PREFIX = "cart:uid:";

    /**
     * 添加单个商品到购物车中,(存入Redis)
     * @param cartItem
     */
    @Override
    public void insertCartItem(CartItem cartItem) {
        initParam();
        String skuId = cartItem.getSkuId().toString();
        if(cart.hasKey(skuId)){
            // 如果购物车中已经有该商品,则逻辑是让该商品的数量加上新增的商品的数量
            CartItem cacheCartItem = JsonUtils.parse(cart.get(skuId).toString(), CartItem.class);
            cartItem.setNum(cartItem.getNum()+cacheCartItem.getNum());
        }

        // 将cartItem存入Redis
        cart.put(skuId, JsonUtils.serialize(cartItem));
    }

    @Override
    public void updateCartItem(Long skuId, int num) {
        initParam();
        CartItem cacheCartItem = JsonUtils.parse(cart.get(skuId.toString()).toString(), CartItem.class);
        // 更新操作要求商品的数量必须大于等于1
        cacheCartItem.setNum(Math.max(1,num));

        //将更新后的数据写会Redis
        cart.put(skuId.toString(),JsonUtils.serialize(cacheCartItem));
    }

    @Override
    public void deleteCartItem(Long skuId) {
        initParam();
        if(cart.hasKey(skuId.toString())){
            cart.delete(skuId.toString());
        }
    }

    @Override
    public List<CartItem> queryCartItemList() {
        initParam();
        List<CartItem> cartList = cart.values().stream()
                .map(c -> JsonUtils.parse(c.toString(), CartItem.class))
                .collect(Collectors.toList());
        return cartList;
    }

    private void initParam(){
        // 获取用户信息
        user = UserInterceptor.getUserInfo();
        uid = user.getId();

        // 获取用户的Redis中的购物车map
        // Redis购物车设计双层Map<K1,Map<K2,V>>,K1为标识用户ID,K2为商品SkuID,V为Cart属性的String值
        cart = redisTemplate.boundHashOps(CARTREDIS_PREFIX + uid);
    }
}
