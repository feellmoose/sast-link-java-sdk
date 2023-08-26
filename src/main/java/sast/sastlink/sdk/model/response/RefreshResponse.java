package sast.sastlink.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * @projectName: sast-link-SDK
 * @author: feelMoose
 * @date: 2023/8/19 21:40
 */
public class RefreshResponse {
    @JsonAlias("access_token")
    private String accessToken;
    @JsonAlias("expires_in")
    private long expiresIn;
    @JsonAlias("refresh_token")
    private String refreshToken;
    private String scope;
    @JsonAlias("token_type")
    private String tokenType;

    public RefreshResponse() {
    }

    public RefreshResponse(String accessToken, long expiresIn, String refreshToken, String scope, String tokenType) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.scope = scope;
        this.tokenType = tokenType;
    }

    /* Getter and Setter */
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        return "RefreshResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                ", scope='" + scope + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}
