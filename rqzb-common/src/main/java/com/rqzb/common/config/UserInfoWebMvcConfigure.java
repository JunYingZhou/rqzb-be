package com.rqzb.common.config;

import com.rqzb.common.interceptor.UserInfoInterceptor;
import org.springframework.core.Ordered;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UserInfoWebMvcConfigure implements WebMvcConfigurer {

    private final UserInfoInterceptor userInfoInterceptor;

/**
 * 构造函数，用于初始化UserInfoWebMvcConfigure实例
 * @param userInfoInterceptor 用户信息拦截器，用于处理用户相关的拦截逻辑
 */
    public UserInfoWebMvcConfigure(UserInfoInterceptor userInfoInterceptor) {
    // 将传入的拦截器赋值给当前类的成员变量
        this.userInfoInterceptor = userInfoInterceptor;
    }

    @Override
/**
 * 添加拦截器
 * @param registry 拦截器注册对象，用于注册自定义拦截器
 */
    public void addInterceptors(InterceptorRegistry registry) {
    // 注册用户信息拦截器，拦截所有请求路径（/**）
    // 设置拦截器顺序为最高优先级
        registry.addInterceptor(userInfoInterceptor)
                .addPathPatterns("/**")  // 拦截所有路径
                .order(Ordered.HIGHEST_PRECEDENCE);  // 设置最高优先级
    }
}
