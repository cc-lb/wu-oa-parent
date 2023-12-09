package com.wu.common.exception;


import com.wu.result.ResultCodeEnum;
import lombok.Data;

/**
 * @Classname WuException
 * @Description
 * @Date 2023/4/17 18:56
 * @Created by cc
 */

/**
 * 处理自定义异常
 */
@Data
public class WuException extends RuntimeException{
    private Integer code;

    private String message;

    public WuException(Integer code,String message){
        super(message);
        this.code=code;
        this.message=message;
    }

    public WuException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code=resultCodeEnum.getCode();
        this.message=resultCodeEnum.getMessage();
    }
    @Override
    public  String toString(){
        return "WuException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
