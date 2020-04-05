package com.leyouxianggou.cart.interceptor;

import com.leyouxianggou.auth.entity.UserInfo;
import com.leyouxianggou.auth.utils.JwtUtils;
import com.leyouxianggou.cart.config.JwtProperties;
import com.leyouxianggou.common.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    private JwtProperties jwtProperties;

    /**
     * 一次请求在一个Tomcat中是同一个线程在处理
     */
    private static final ThreadLocal<UserInfo> threadLocal = new ThreadLocal<>();

    public UserInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取cookie中的Token
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());

        // 解析token
        try {
            UserInfo userInfo = JwtUtils.getUserInfo(jwtProperties.getPublicKey(), token);
            // 将UserInfo向下传递到Controller和Service中去
            threadLocal.set(userInfo);
            // 解析成功，放行
            return true;
        }catch (Exception e){
            // 解析失败
            log.error("[购物车微服务]:用户Token解析失败!",e);
            return false;
        }
    }

    /**
     * 当Controller处理完后的回调方法，清除TheadLocal中的数据
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        threadLocal.remove();
    }

    /**
     * 从TheadLocal中获取UserInfo对象
     * @return
     */
    public static UserInfo getUserInfo(){
        return threadLocal.get();
    }
}
