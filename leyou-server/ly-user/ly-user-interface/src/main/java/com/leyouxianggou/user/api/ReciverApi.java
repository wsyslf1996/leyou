package com.leyouxianggou.user.api;

import com.leyouxianggou.user.pojo.Receiver;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface ReciverApi {
    @GetMapping("/list")
    List<Receiver> queryReciverListByUserId(Long uid);
}
