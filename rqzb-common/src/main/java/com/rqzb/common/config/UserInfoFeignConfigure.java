package com.rqzb.common.config;

import com.rqzb.common.context.LoginUser;
import com.rqzb.common.context.UserContext;
import com.rqzb.common.context.UserInfoHeaders;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserInfoFeignConfigure {

    @Bean
    public RequestInterceptor userInfoRequestInterceptor() {
        return requestTemplate -> {
            LoginUser loginUser = UserContext.get();
            if (loginUser == null || loginUser.getUserId() == null) {
                return;
            }

            requestTemplate.removeHeader(UserInfoHeaders.USER_ID);
            requestTemplate.removeHeader(UserInfoHeaders.USERNAME);
            requestTemplate.removeHeader(UserInfoHeaders.NICKNAME);
            requestTemplate.header(UserInfoHeaders.USER_ID, String.valueOf(loginUser.getUserId()));
            if (UserInfoHeaders.hasText(loginUser.getUsername())) {
                requestTemplate.header(UserInfoHeaders.USERNAME, UserInfoHeaders.encode(loginUser.getUsername()));
            }
            if (UserInfoHeaders.hasText(loginUser.getNickname())) {
                requestTemplate.header(UserInfoHeaders.NICKNAME, UserInfoHeaders.encode(loginUser.getNickname()));
            }
        };
    }
}
