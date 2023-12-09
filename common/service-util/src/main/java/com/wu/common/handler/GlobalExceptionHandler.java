package com.wu.common.handler;

import com.wu.common.exception.WuException;
import com.wu.result.Result;
import com.wu.result.ResultCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * @Classname GlobalExceptionHandler
 * @Description
 * @Date 2023/4/17 17:50
 * @Created by cc
 */

/**
 * 处理全局异常
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result erro(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(WuException.class)
    @ResponseBody
    public Result erro(WuException e){
        e.printStackTrace();
        return Result.fail().message(e.getMessage()).code(e.getCode());
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.fail().message("执行了特定异常处理");
    }


   @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result error(AccessDeniedException e) throws AccessDeniedException {
        return Result.fail().code(205).message("没有权限");
}
}
