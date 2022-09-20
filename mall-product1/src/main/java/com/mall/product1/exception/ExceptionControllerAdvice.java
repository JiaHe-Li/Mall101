package com.mall.product1.exception;

import com.mall.common.exception.BizCodeEnum;
import com.mall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
//@ResponseBody //返回json数据
//@ControllerAdvice(basePackages = "com.mall.product1.controller")
@RestControllerAdvice(basePackages = "com.mall.product1.controller")
public class ExceptionControllerAdvice {

    //状态码封装在common包的exception中了
    @ExceptionHandler(value = MethodArgumentNotValidException.class)//指定接收什么类型的异常
    public R handleValidException(MethodArgumentNotValidException e){
        log.error("shujuchuwentile{},异常类型{}",e.getMessage(),e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String,String> map = new HashMap<>();
           //1.获取校验错误的结果
            bindingResult.getFieldErrors().forEach((item) -> {
                map.put(item.getField(),item.getDefaultMessage());
            });
        return R.error(BizCodeEnum.VAILD_EXCEPTION.getCode(),BizCodeEnum.VAILD_EXCEPTION.getMessage()).put("data",map);
    }
    //处理其他所有前面不能处理的错误
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        log.info("当前错误信息"+ throwable.toString());
        return R.error(BizCodeEnum.UNKNOW_EXCEPTION.getCode(), BizCodeEnum.UNKNOW_EXCEPTION.getMessage());
    }
}
