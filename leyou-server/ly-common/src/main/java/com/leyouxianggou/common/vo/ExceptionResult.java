package com.leyouxianggou.common.vo;

import com.leyouxianggou.common.enums.ExceptionEnum;
import lombok.Data;

@Data
public class ExceptionResult {
    public ExceptionResult(ExceptionEnum em){
        this.status = em.getCode();
        this.message=em.getMsg();
        this.timstamp=System.currentTimeMillis();
    }
    private int status;
    private String message;
    private Long timstamp;
}
