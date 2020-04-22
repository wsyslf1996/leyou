package com.leyouxianggou.user.controller;

import com.leyouxianggou.auth.entity.UserInfo;
import com.leyouxianggou.user.interceptor.UserInterceptor;
import com.leyouxianggou.user.pojo.Receiver;
import com.leyouxianggou.user.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/receiver")
public class ReceiverController {

    @Autowired
    private ReceiverService receiverService;

    /**
     * 查询当前用户的所有收件人
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<Receiver>> queryReceiverListByUserId(){
        UserInfo user = UserInterceptor.getUserInfo();
        Long uid = user.getId();
        return ResponseEntity.ok(receiverService.queryReceiverListByUserId(uid));
    }

    @PostMapping
    public ResponseEntity<Void> insertReceiver(@RequestBody Receiver receiver){
        UserInfo user = UserInterceptor.getUserInfo();
        Long uid = user.getId();
        receiver.setUserId(uid);
        receiverService.insertReceiver(receiver);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Receiver> queryReceiver(@PathVariable("id")Long receiverId){
        return ResponseEntity.ok(receiverService.queryReceiver(receiverId));
    }

    @PutMapping
    public ResponseEntity<Void> updateReceiver(@RequestBody Receiver receiver){
        UserInfo user = UserInterceptor.getUserInfo();
        Long uid = user.getId();
        receiver.setUserId(uid);
        receiverService.updateReceiver(receiver);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceiver(@PathVariable("id")Long receiverId){
        receiverService.deleteReceiver(receiverId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
