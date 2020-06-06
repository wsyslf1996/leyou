package com.leyouxianggou.user.api;

import com.leyouxianggou.user.pojo.Receiver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ReceiverApi {
    @GetMapping("/receiver/list")
    List<Receiver> queryReciverListByUserId(Long uid);

    @GetMapping("/receiver/{id}")
    Receiver queryReceiver(@PathVariable("id")Long receiverId);
}
