package com.leyouxianggou.search.client;

import com.leyouxianggou.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
