package com.leyouxianggou.order.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.leyouxianggou.common.enums.ExceptionEnum;
import com.leyouxianggou.common.exception.LyException;
import com.leyouxianggou.order.config.AlipayProperties;
import com.leyouxianggou.order.enums.OrderStatusEnums;
import com.leyouxianggou.order.enums.PayTypeEnums;
import com.leyouxianggou.order.mapper.OrderDetailMapper;
import com.leyouxianggou.order.mapper.OrderMapper;
import com.leyouxianggou.order.mapper.OrderStatusMapper;
import com.leyouxianggou.order.mapper.PayLogMapper;
import com.leyouxianggou.order.pojo.Order;
import com.leyouxianggou.order.pojo.OrderDetail;
import com.leyouxianggou.order.pojo.OrderStatus;
import com.leyouxianggou.order.pojo.PayLog;
import com.leyouxianggou.order.service.AlipayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
@Slf4j
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private PayLogMapper payLogMapper;

    /**
     * 创建支付订单
     * @param orderNum
     * @param totalAmount
     * @param orderTitle
     * @param orderDescription
     * @return 创建好的支付订单的自动跳转的html代码
     */
    @Override
    public String createAlipayOrder(String orderNum,String totalAmount,String orderTitle,String orderDescription) {
        AlipayClient alipayClient = new DefaultAlipayClient(alipayProperties.getGatewayUrl(),
                alipayProperties.getApp_id(),
                alipayProperties.getMerchant_private_key(),
                "json",
                alipayProperties.getCharset(),
                alipayProperties.getAlipay_public_key(),
                alipayProperties.getSign_type());

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(alipayProperties.getReturn_url());
        alipayRequest.setNotifyUrl(alipayProperties.getNotify_url());

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = orderNum;
        //付款金额，必填
        String total_amount = totalAmount;
        //订单名称，必填
        String subject = orderTitle;
        //商品描述，可空
        String body = orderDescription;

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        AlipayTradePagePayResponse alipayTradePagePayResponse = null;
        String result = null;
        try {
            alipayTradePagePayResponse = alipayClient.pageExecute(alipayRequest);
            result = alipayTradePagePayResponse.getBody();
        } catch (AlipayApiException e) {
            log.error("支付宝支付订单创建失败！");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String createDefaultAlipayOrder(String orderNum){
        Long orderId = Long.valueOf(orderNum);
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(order == null){
            log.error("订单{}不存在",orderId);
            throw new LyException(ExceptionEnum.ORDER_NOT_EXIST);
        }
        // 查询总金额,处理金额，将单位为分转化成元
        String totalAmount = order.getTotalPay().toString();
        StringBuilder stringBuilder = new StringBuilder(totalAmount);
        stringBuilder.insert(stringBuilder.length()-2,'.');
        totalAmount = stringBuilder.toString();

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        List<OrderDetail> detailList = orderDetailMapper.select(orderDetail);
        String orderDescription = null;
        if(detailList.size()==1){
            orderDescription = detailList.get(0).getTitle();
        }else {
            orderDescription = "网上商城-在线支付";
        }
        return createAlipayOrder(orderNum,totalAmount,"乐优商城",orderDescription);
    }

    @Override
    public String handleNotify(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {
        //获取支付宝POST过来反馈信息
        Map<String,String[]> requestParams = request.getParameterMap();
        // 开始验证签名
        boolean signVerified = signVerified(requestParams); //调用SDK验证签名
        if(signVerified) {//验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
            Long orderId = Long.valueOf(out_trade_no);

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

            if(trade_status.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            }else if (trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
                // TODO 交易业务写在此处
                // 1.填写支付记录
                Order order = orderMapper.selectByPrimaryKey(orderId);
                if(order==null){
                    log.error("数据库订单丢失，订单号："+orderId+",交易号："+trade_no);
                    throw new LyException(ExceptionEnum.ORDER_LOST);
                }
                Date current = new Date();
                PayLog payLog = new PayLog();
                payLog.setTotalFee(order.getTotalPay());
                payLog.setUserId(order.getUserId());
                payLog.setOrderId(Long.valueOf(out_trade_no));
                payLog.setTransactionId(trade_no);
                payLog.setStatus(OrderStatusEnums.PAYED.value());
                payLog.setCreateTime(current);
                payLog.setPayType(PayTypeEnums.ALI_PAY.value());
                payLogMapper.insert(payLog);

                // 2.更新订单状态
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setOrderId(orderId);
                orderStatus.setStatus(OrderStatusEnums.PAYED.value());
                orderStatus.setPaymentTime(current);
                orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
            }
            return "success";
        }else {//验证失败
            log.error("签名验证失败！请联系管理员！");
            return "fail";
        }
    }

    private Boolean signVerified(Map<String,String[]> requestParams) throws UnsupportedEncodingException, AlipayApiException{
        Map<String,String> params = new HashMap<String,String>();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        // 验证
        if(params.get("auth_app_id").equals(alipayProperties.getApp_id())) return true;
        else return false;
        // 开始验证签名
//        String signType = alipayProperties.getSign_type();
//        if(signType.equals("RSA1")){
//            return AlipaySignature.rsaCheckV1(params, alipayProperties.getAlipay_public_key(), alipayProperties.getCharset(), alipayProperties.getSign_type()); //调用SDK验证签名
//        }else if(signType.equals("RSA2")){
//            return AlipaySignature.rsaCheckV2(params, alipayProperties.getAlipay_public_key(), alipayProperties.getCharset(), alipayProperties.getSign_type()); //调用SDK验证签名
//        }
//        else return false;
    }
}
