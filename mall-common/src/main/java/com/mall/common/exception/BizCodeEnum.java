package com.mall.common.exception;


import lombok.extern.slf4j.Slf4j;


public enum BizCodeEnum {
    UNKNOW_EXCEPTION(10000, "系统未知异常"),
    VAILD_EXCEPTION(100001,"参数格式校验失败");

    private int code;
    private String message;

    BizCodeEnum(int code,String message){
        this.code=code;
        this.message=message;

    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
