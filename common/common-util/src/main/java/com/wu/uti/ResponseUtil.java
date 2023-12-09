package com.wu.uti;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wu.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname ResponseUtil
 * @Description
 * @Date 2023/5/18 14:30
 * @Created by cc
 */
public class ResponseUtil {
    public static void out(HttpServletResponse response, Result r) {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try {

            mapper.writeValue(response.getWriter(), r);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
