package sast.sastlink.sdk.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import sast.sastlink.sdk.enums.GrantType;
import sast.sastlink.sdk.enums.SastLinkApi;
import sast.sastlink.sdk.exception.SastLinkException;
import sast.sastlink.sdk.exception.errors.SastLinkErrorEnum;
import sast.sastlink.sdk.model.response.SastLinkResponse;
import sast.sastlink.sdk.model.response.data.AccessToken;
import sast.sastlink.sdk.model.response.data.RefreshToken;
import sast.sastlink.sdk.model.response.data.User;
import sast.sastlink.sdk.service.AbstractSastLinkService;
import sast.sastlink.sdk.util.JsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientSastLinkService extends AbstractSastLinkService<HttpClientSastLinkService> {
    private final HttpClient httpClient;

    public static HttpClientSastLinkService.Builder Builder() {
        return new HttpClientSastLinkService.Builder();
    }

    protected HttpClientSastLinkService(Builder builder) {
        super(builder);
        this.httpClient = builder.httpClient;
    }

    @Override
    public AccessToken accessToken(String code) throws SastLinkException {
        String str = SastLinkApi.ACCESS_TOKEN.getHttp(host_name) +
                "?code=" + code +
                "&code_verifier=" + this.code_verifier +
                "&grant_type=" + GrantType.AUTHORIZATION_CODE.name +
                "&redirect_uri=" + this.redirect_uri +
                "&client_id=" + this.client_id +
                "&client_secret=" + this.client_secret;
        HttpRequest request = HttpRequest.newBuilder(URI.create(str))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        AccessToken accessToken;
        try {
            accessToken = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body().transform(body -> {
                if (body.isEmpty()) throw new SastLinkException(SastLinkErrorEnum.EMPTY_RESPONSE_BODY);
                SastLinkResponse<AccessToken> response = JsonUtil.fromJson(body, new TypeReference<>() {
                });
                if (!response.isSuccess()) throw new SastLinkException(response);
                return response.getData();
            });
        } catch (IOException e) {
            throw new SastLinkException(SastLinkErrorEnum.IO_ERROR, e);
        } catch (InterruptedException e) {
            throw new SastLinkException(e);
        }
        return accessToken;
    }

    @Override
    public RefreshToken refreshToken(String refreshToken) throws SastLinkException {
        String str = SastLinkApi.REFRESH.getHttp(host_name) +
                "?refresh_token=" + refreshToken +
                "&grant_type=" + GrantType.REFRESH_TOKEN.name;
        HttpRequest request = HttpRequest.newBuilder(URI.create(str))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        RefreshToken refresh;
        try {
            refresh = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body().transform(body -> {
                if (body.isEmpty()) throw new SastLinkException(SastLinkErrorEnum.EMPTY_RESPONSE_BODY);
                SastLinkResponse<RefreshToken> response = JsonUtil.fromJson(body, new TypeReference<>() {
                });
                if (!response.isSuccess()) throw new SastLinkException(response);
                return response.getData();
            });
        } catch (IOException e) {
            throw new SastLinkException(SastLinkErrorEnum.IO_ERROR, e);
        } catch (InterruptedException e) {
            throw new SastLinkException(e);
        }
        return refresh;
    }

    @Override
    public User user(String accessToken) throws SastLinkException {
        HttpRequest request = HttpRequest.newBuilder(SastLinkApi.USER_INFO.getHttpURI(host_name))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();
        User user;
        try {
            user = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body().transform(body -> {
                if (body.isEmpty()) throw new SastLinkException(SastLinkErrorEnum.EMPTY_RESPONSE_BODY);
                SastLinkResponse<User> response = JsonUtil.fromJson(body, new TypeReference<>() {
                });
                if (!response.isSuccess()) throw new SastLinkException(response);
                return response.getData();
            });
        } catch (IOException e) {
            throw new SastLinkException(SastLinkErrorEnum.IO_ERROR, e);
        } catch (InterruptedException e) {
            throw new SastLinkException(e);
        }
        return user;
    }

    public static class Builder extends AbstractSastLinkService.Builder<HttpClientSastLinkService> {
        private HttpClient httpClient;

        private Builder setHttpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        private Builder() {
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public HttpClientSastLinkService build() {
            super.build();
            if (this.httpClient == null) {
                this.httpClient = HttpClient.newHttpClient();
            }
            return new HttpClientSastLinkService(this);
        }
    }
}
