package com.leyouxianggou.user.service;

import com.leyouxianggou.user.pojo.Receiver;

import java.util.List;

public interface ReceiverService {
    List<Receiver> queryReceiverListByUserId(Long uid);

    Receiver queryReceiver(Long receiverId);

    void insertReceiver(Receiver receiver);

    void deleteReceiver(Long receiverId);

    void updateReceiver(Receiver receiver);
}
