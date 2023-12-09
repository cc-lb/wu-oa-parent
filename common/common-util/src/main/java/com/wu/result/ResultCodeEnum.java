package com.wu.result;

import lombok.Getter;

/**
 * @Classname ResultCodeEnum
 * @Description
 * @Date 2023/3/28 21:13
 * @Created by cc
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    LOGIN_ERROR(206,"认证失败"),
    LOGIN_MOBEL_ERROR(207,"登录认证失败"),
    PERMISSION(208,"token认证失败")
            ;


    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code,String message) {
        this.code = code;
        this.message = message;
    }
}
