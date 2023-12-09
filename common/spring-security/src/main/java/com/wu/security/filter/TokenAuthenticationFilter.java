package com.wu.security.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.wu.jwt.JwtHelper;
import com.wu.result.Result;
import com.wu.result.ResultCodeEnum;
import com.wu.security.hlper.LoginInfoHelper;
import com.wu.uti.ResponseUtil;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Classname TokenAuthenticationFilter
 * @Description
 * @Date 2023/5/18 14:43
 * @Created by cc
 */

/**
 * 认证解析token过滤器
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;
    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.info("uri:"+request.getRequestURI());

        if(request.getRequestURI().equals("/admin/system/index/login")){
            filterChain.doFilter(request, response);
             return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthtication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.PERMISSION));
        }
        //logger.info(response.getWriter());
    }

    //这里只返回了用户名
    private UsernamePasswordAuthenticationToken getAuthtication(HttpServletRequest request) {
        String token = request.getHeader("token");
        logger.info("从header取出的token："+token);
        if(!StringUtils.isEmpty(token)){
            LoginInfoHelper.setUserId( JwtHelper.getUserId(token));
            LoginInfoHelper.setUsername(JwtHelper.getUsername(token));
            System.out.println("ThreadLocal中存入的id：  "+LoginInfoHelper.getUserId());
            String username = JwtHelper.getUsername(token);
            logger.info("用户名："+username);
            if(!StringUtils.isEmpty(username)){
                redisTemplate.setKeySerializer(RedisSerializer.string());
                redisTemplate.setValueSerializer(RedisSerializer.string());
                String authoritiesString =(String) redisTemplate.opsForValue().get(username.trim());
                logger.info("redis中取出数据："+authoritiesString.toString());
                List<Map> mapList= JSON.parseArray(authoritiesString);
                ArrayList<SimpleGrantedAuthority> authorityArrayList = new ArrayList<>();
                for (Map map:mapList) {
                    authorityArrayList.add(new SimpleGrantedAuthority((String) map.get("authority")));
                }

                return new UsernamePasswordAuthenticationToken(username,null, authorityArrayList);
            }else {
                return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());

            }

        }
return null;

    }

}
