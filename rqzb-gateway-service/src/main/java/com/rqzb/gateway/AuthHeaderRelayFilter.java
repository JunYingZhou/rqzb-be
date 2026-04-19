package com.rqzb.gateway;

import cn.dev33.satoken.jwt.SaJwtUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONObject;
import com.rqzb.common.context.LoginUser;
import com.rqzb.common.context.UserInfoHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthHeaderRelayFilter implements GlobalFilter, Ordered {

    private static final String SA_TOKEN_HEADER = "satoken";
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${sa-token.jwt-secret-key:}")
    private String jwtSecretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = resolveToken(request.getHeaders());
        LoginUser loginUser = parseLoginUser(token);

        ServerHttpRequest mutatedRequest = request.mutate()
                .headers(headers -> {
                    removeUserHeaders(headers);
                    if (StringUtils.hasText(token)) {
                        headers.set(SA_TOKEN_HEADER, token);
                    }
                    if (loginUser != null) {
                        writeUserHeaders(headers, loginUser);
                    }
                })
                .build();
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private String resolveToken(HttpHeaders headers) {
        String token = headers.getFirst(SA_TOKEN_HEADER);
        if (StringUtils.hasText(token)) {
            return token;
        }

        String authorization = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authorization)
                || !authorization.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())) {
            return null;
        }

        token = authorization.substring(BEARER_PREFIX.length()).trim();
        return StringUtils.hasText(token) ? token : null;
    }

    private LoginUser parseLoginUser(String token) {
        if (!StringUtils.hasText(token) || !StringUtils.hasText(jwtSecretKey)) {
            return null;
        }
        try {
            Object loginId = SaJwtUtil.getLoginIdOrNull(token, StpUtil.TYPE, jwtSecretKey);
            if (loginId == null) {
                return null;
            }
            JSONObject payloads = SaJwtUtil.getPayloads(token, StpUtil.TYPE, jwtSecretKey);
            return new LoginUser(
                    Long.valueOf(loginId.toString()),
                    payloads.getStr(UserInfoHeaders.CLAIM_USERNAME),
                    payloads.getStr(UserInfoHeaders.CLAIM_NICKNAME)
            );
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private void removeUserHeaders(HttpHeaders headers) {
        headers.remove(UserInfoHeaders.USER_ID);
        headers.remove(UserInfoHeaders.USERNAME);
        headers.remove(UserInfoHeaders.NICKNAME);
    }

    private void writeUserHeaders(HttpHeaders headers, LoginUser loginUser) {
        headers.set(UserInfoHeaders.USER_ID, String.valueOf(loginUser.getUserId()));
        if (UserInfoHeaders.hasText(loginUser.getUsername())) {
            headers.set(UserInfoHeaders.USERNAME, UserInfoHeaders.encode(loginUser.getUsername()));
        }
        if (UserInfoHeaders.hasText(loginUser.getNickname())) {
            headers.set(UserInfoHeaders.NICKNAME, UserInfoHeaders.encode(loginUser.getNickname()));
        }
    }
}
