package com.spring.security.postgresql.payload.response;

import java.util.List;

public class JwtResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private List<String> roles;
    private String message;

    public JwtResponse(String accessToken, String message, List<String> roles) {
        this.accessToken = accessToken;
        this.message = message;
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
