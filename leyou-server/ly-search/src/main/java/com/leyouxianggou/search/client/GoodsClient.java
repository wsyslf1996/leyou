package com.leyouxianggou.search.client;

import com.leyouxianggou.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
