package com.leyouxianggou.gateway.filter;

import com.leyouxianggou.auth.entity.UserInfo;
import com.leyouxianggou.auth.utils.JwtUtils;
import com.leyouxianggou.common.utils.CookieUtils;
import com.leyouxianggou.gateway.config.FilterProperties;
import com.leyouxianggou.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Component
@EnableConfigurationProperties({JwtProperties.class,FilterProperties.class})
public class AuthFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        String requestURI = ctx.getRequest().getRequestURI();
        // 设置请求过滤白名单
        return !isAllowPath(requestURI);
    }

    private boolean isAllowPath(String requestURI) {
        List<String> allowPaths = filterProperties.getAllowPaths();
        for (String preUri: allowPaths){
            if(requestURI.startsWith(preUri))
                return true;
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();

        // 获取Request对象
        HttpServletRequest request = ctx.getRequest();
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        try {
            // 解析通过说明token是正确的
            UserInfo userInfo = JwtUtils.getUserInfo(jwtProperties.getPublicKey(), token);
            // TODO 校验权限

        }catch (Exception e){
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.SC_FORBIDDEN);
            log.error("非法访问，无权限访问当前URL:{}",request.getRequestURL(),e);
        }
        return null;
    }
}
