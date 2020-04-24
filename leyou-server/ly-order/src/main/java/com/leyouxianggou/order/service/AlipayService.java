package com.leyouxianggou.order.service;

import com.alipay.api.AlipayApiException;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface AlipayService {
    String createAlipayOrder(String orderNum,String totalAmount,String orderTitle,String orderDescription);

    String createDefaultAlipayOrder(String orderNum);

    String handleNotify(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException;
}
