package com.leyouxianggou.cart.controller;

import com.leyouxianggou.cart.pojo.CartItem;
import com.leyouxianggou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Void> insertCartItem(@RequestBody CartItem cartItem){
        cartService.insertCartItem(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 查询购物车商品
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<CartItem>> queryCartItemList(){
        return ResponseEntity.ok(cartService.queryCartItemList());
    }

    /**
     * 修改购物车商品
     */
    @PutMapping
    public ResponseEntity<Void> updateCartItem(@RequestParam("id")Long skuId,
                                               @RequestParam("num")int num){
        cartService.updateCartItem(skuId,num);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{skuId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable("skuId") Long skuId){
        cartService.deleteCartItem(skuId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
