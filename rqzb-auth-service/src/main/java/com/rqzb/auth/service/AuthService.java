package com.rqzb.auth.service;

import com.rqzb.auth.dto.CurrentUserResponse;
import com.rqzb.auth.dto.LoginRequest;
import com.rqzb.auth.dto.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    LoginResponse login(LoginRequest request, HttpServletRequest servletRequest);

    void logout();

    CurrentUserResponse currentUser();
}
