package com.leyouxianggou.user.service.impl;

import com.leyouxianggou.common.enums.ExceptionEnum;
import com.leyouxianggou.common.exception.LyException;
import com.leyouxianggou.user.mapper.ReceiverMapper;
import com.leyouxianggou.user.pojo.Receiver;
import com.leyouxianggou.user.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ReceiverServiceImpl implements ReceiverService {

    @Autowired
    private ReceiverMapper receiverMapper;

    @Override
    public List<Receiver> queryReceiverListByUserId(Long uid) {
        Receiver receiver = new Receiver();
        receiver.setUserId(uid);
        List<Receiver> receivers = receiverMapper.select(receiver);
        if(CollectionUtils.isEmpty(receivers)){
            throw new LyException(ExceptionEnum.RECEIVER_NOT_FOUND);
        }
        return receivers;
    }

    @Override
    public void insertReceiver(Receiver receiver) {
        // 如果新增的为默认地址则先恢复用户的默认地址全为false
        if(receiver.getDefaultAddress()){
            receiverMapper.restoreDefaultAddressTag(receiver.getUserId());
        }
        int count = receiverMapper.insert(receiver);
        if(count!=1){
            throw new LyException(ExceptionEnum.RECEIVER_INSERT_ERROR);
        }
    }

    @Override
    public Receiver queryReceiver(Long receiverId) {
        Receiver receiver = receiverMapper.selectByPrimaryKey(receiverId);
        if(receiver == null){
            throw new LyException(ExceptionEnum.RECEIVER_NOT_FOUND);
        }
        return receiver;
    }

    @Override
    public void updateReceiver(Receiver receiver) {
        // 如果修改的为默认地址则先恢复用户的默认地址为false
        if(receiver.getDefaultAddress()){
            receiverMapper.restoreDefaultAddressTag(receiver.getUserId());
        }
        int count = receiverMapper.updateByPrimaryKey(receiver);
        if(count!=1){
            throw new LyException(ExceptionEnum.RECEIVER_UPDATE_ERROR);
        }
    }

    @Override
    public void deleteReceiver(Long receiverId) {
        int count = receiverMapper.deleteByPrimaryKey(receiverId);
        if(count!=1){
            throw new LyException(ExceptionEnum.RECEIVER_DELETE_ERROR);
        }
    }
}
