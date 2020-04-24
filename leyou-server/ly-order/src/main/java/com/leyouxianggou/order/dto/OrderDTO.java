package com.leyouxianggou.order.dto;

import com.leyouxianggou.item.CartDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderDTO {
    @NotNull
    private Long receiverId; // 收件人ID
    @NotNull
    private List<CartDTO> carts; // 购物车商品
    @NotNull
    private Integer paymentType; // 支付类型
}
