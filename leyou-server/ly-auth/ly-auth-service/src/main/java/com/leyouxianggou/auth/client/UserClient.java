package com.leyouxianggou.auth.client;

import com.leyouxianggou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("user-service")
public interface UserClient extends UserApi {
}
