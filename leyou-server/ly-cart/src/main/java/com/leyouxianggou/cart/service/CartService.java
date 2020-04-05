package com.leyouxianggou.cart.service;

import com.leyouxianggou.cart.pojo.CartItem;

import java.util.List;

public interface CartService {
    void insertCartItem(CartItem cartItem);

    List<CartItem> queryCartItemList();

    void updateCartItem(Long skuId, int num);

    void deleteCartItem(Long skuId);
}
