package com.rqzb.common.interceptor;

import com.rqzb.common.context.LoginUser;
import com.rqzb.common.context.UserContext;
import com.rqzb.common.context.UserInfoHeaders;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserInfoInterceptor implements HandlerInterceptor {

    @Override
    /**
     * 前置处理器方法，在请求处理之前执行
     * @param request 当前HTTP请求对象，包含请求信息
     * @param response 当前HTTP响应对象，用于生成响应
     * @param handler 请求处理的方法处理器
     * @return 返回true表示继续流程，false表示终端流程
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从请求头中获取登录用户信息并设置到用户上下文中
        LoginUser loginUser = UserInfoHeaders.fromHeaders(request::getHeader);
        UserContext.set(loginUser);
        return true;
    }

/**
 * 在请求完成后执行的方法，用于清理用户上下文信息
 * 该方法是拦截器的一部分，在请求处理完成后被调用
 *
 * @param request HttpServletRequest对象，包含当前请求的信息
 * @param response HttpServletResponse对象，用于生成响应
 * @param handler 请求处理的方法处理器
 * @param ex 处理过程中发生的异常，如果没有异常则为null
 */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    // 清除用户上下文信息，防止内存泄漏
        UserContext.clear();
    }
}
