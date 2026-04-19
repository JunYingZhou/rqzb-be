package com.rqzb.common.context;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public final class UserInfoHeaders {

    public static final String USER_ID = "X-User-Id";

    public static final String USERNAME = "X-User-Name";

    public static final String NICKNAME = "X-User-Nickname";

    public static final String CLAIM_USERNAME = "username";

    public static final String CLAIM_NICKNAME = "nickname";

    private UserInfoHeaders() {
    }

    public static LoginUser fromHeaders(Function<String, String> headerGetter) {
        Long userId = parseLong(headerGetter.apply(USER_ID));
        if (userId == null) {
            return null;
        }
        return new LoginUser(
                userId,
                decode(headerGetter.apply(USERNAME)),
                decode(headerGetter.apply(NICKNAME))
        );
    }

    public static String encode(String value) {
        if (!hasText(value)) {
            return value;
        }
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public static String decode(String value) {
        if (!hasText(value)) {
            return value;
        }
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ex) {
            return value;
        }
    }

    public static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private static Long parseLong(String value) {
        if (!hasText(value)) {
            return null;
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
