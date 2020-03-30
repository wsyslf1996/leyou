package com.leyouxianggou.client;

import com.leyouxianggou.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
