package com.wu.security.custom;

import com.wu.md5.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @Classname CustomMd5PasswordEncoder
 * @Description
 * @Date 2023/5/15 21:17
 * @Created by cc
 */
@Component
public class CustomMd5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return MD5.encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5.encrypt(rawPassword.toString()));
    }
}
