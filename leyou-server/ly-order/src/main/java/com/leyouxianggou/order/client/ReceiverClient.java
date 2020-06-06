package com.leyouxianggou.order.client;

import com.leyouxianggou.user.api.ReceiverApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("user-service")
public interface ReceiverClient extends ReceiverApi {
}
