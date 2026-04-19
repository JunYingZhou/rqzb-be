package com.rqzb.common.context;

public final class UserContext {

    private static final ThreadLocal<LoginUser> USER_HOLDER = new ThreadLocal<>();

    private UserContext() {
    }

    public static void set(LoginUser loginUser) {
        if (loginUser == null) {
            clear();
            return;
        }
        USER_HOLDER.set(loginUser);
    }

    public static LoginUser get() {
        return USER_HOLDER.get();
    }

    public static Long getUserId() {
        LoginUser loginUser = get();
        return loginUser == null ? null : loginUser.getUserId();
    }

    public static String getUsername() {
        LoginUser loginUser = get();
        return loginUser == null ? null : loginUser.getUsername();
    }

    public static String getNickname() {
        LoginUser loginUser = get();
        return loginUser == null ? null : loginUser.getNickname();
    }

    public static void clear() {
        USER_HOLDER.remove();
    }
}
