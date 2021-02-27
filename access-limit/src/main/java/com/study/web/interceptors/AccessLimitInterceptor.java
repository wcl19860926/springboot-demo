package com.study.web.interceptors;

import com.study.annotation.AccessLimit;
import com.study.redis.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Component
public class AccessLimitInterceptor extends HandlerInterceptorAdapter {

    private static String key_prefix = "acc_count";


    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断请求是否属于方法的请求
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            //获取方法中的注解,看是否有该注解
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean login = accessLimit.needLogin();
            String key = request.getRequestURI();

            String sessionId = request.getSession().getId();
            String accessCountKey = key_prefix + sessionId;

            //从redis中获取用户访问的次数
            Integer count = (Integer) redisCacheService.get(accessCountKey);

            if (count == null) {
                //第一次访问
                count = 1;
                redisCacheService.set(accessCountKey, count, 60);
            } else if (count < maxCount) {
                count++;
                //加1
                redisCacheService.increment(accessCountKey);
            } else {
                //超出访问次数
                render(response, "{msg:\"超过最大访问次数，禁止访问！\"}"); //这里的CodeMsg是一个返回参数
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, String msg) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        out.write(msg.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }

}

