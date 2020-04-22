package com.leyouxianggou.user.mapper;

import com.leyouxianggou.user.pojo.Receiver;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface ReceiverMapper extends Mapper<Receiver> {
    @Update("update tb_receiver set default_address = false where user_id = #{user_id} and default_address = true")
    int restoreDefaultAddressTag(@Param("user_id") Long uid);
}
