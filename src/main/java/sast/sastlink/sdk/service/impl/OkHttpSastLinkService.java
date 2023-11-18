package sast.sastlink.sdk.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.*;
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

public class OkHttpSastLinkService extends AbstractSastLinkService<OkHttpSastLinkService> {
    private OkHttpClient okHttpClient;

    public static OkHttpSastLinkService.Builder Builder() {
        return new OkHttpSastLinkService.Builder();
    }

    protected OkHttpSastLinkService(Builder builder) {
        super(builder);
        this.okHttpClient = builder.okHttpClient;
    }

    @Override
    public AccessToken accessToken(String code) throws SastLinkException {
        HttpUrl url = HttpUrl.get(SastLinkApi.ACCESS_TOKEN.getHttpURI(host_name)).newBuilder()
                .addQueryParameter("code", code)
                .addQueryParameter("code_verifier", this.code_verifier)
                .addQueryParameter("grant_type", GrantType.AUTHORIZATION_CODE.name)
                .addQueryParameter("redirect_uri", this.redirect_uri)
                .addQueryParameter("client_id", this.client_id)
                .addQueryParameter("client_secret", this.client_secret)
                .build();
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .url(url.url())
                .method("POST", RequestBody.create("".getBytes()))
                .build();
        String body = exchangeForResponseBody(request);
        SastLinkResponse<AccessToken> response = JsonUtil.fromJson(body, new TypeReference<>() {
        });
        if (!response.isSuccess()) {
            throw new SastLinkException(response);
        }
        return response.getData();
    }

    @Override
    public RefreshToken refreshToken(String refreshToken) throws SastLinkException {
        HttpUrl url = HttpUrl.get(SastLinkApi.REFRESH.getHttpURI(host_name)).newBuilder()
                .addQueryParameter("refresh_token", refreshToken)
                .addQueryParameter("grant_type", GrantType.REFRESH_TOKEN.name)
                .build();
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .url(url.url())
                .method("POST", RequestBody.create("".getBytes()))
                .build();
        String body = exchangeForResponseBody(request);
        SastLinkResponse<RefreshToken> response = JsonUtil.fromJson(body, new TypeReference<>() {
        });
        if (!response.isSuccess()) {
            throw new SastLinkException(response);
        }
        return response.getData();
    }

    @Override
    public User user(String accessToken) throws SastLinkException {
        HttpUrl url = HttpUrl.get(SastLinkApi.USER_INFO.getHttpURI(host_name)).newBuilder().build();
        Request request = new Request.Builder()
                .header("Authorization", "Bearer " + accessToken)
                .url(url.url())
                .get()
                .build();
        String body = exchangeForResponseBody(request);
        SastLinkResponse<User> response = JsonUtil.fromJson(body, new TypeReference<>() {
        });
        if (!response.isSuccess()) {
            throw new SastLinkException(response);
        }
        return response.getData();
    }

    private String exchangeForResponseBody(Request request) {
        String body;
        try {
            ResponseBody responseBody = okHttpClient.newCall(request).execute().body();
            if (responseBody == null) {
                throw new SastLinkException(SastLinkErrorEnum.NULL_RESPONSE_BODY);
            }
            body = responseBody.string();
            if (body.isEmpty()) {
                throw new SastLinkException(SastLinkErrorEnum.EMPTY_RESPONSE_BODY);
            }
        } catch (IOException e) {
            throw new SastLinkException(SastLinkErrorEnum.IO_ERROR, e);
        }
        return body;
    }

    public static class Builder extends AbstractSastLinkService.Builder<OkHttpSastLinkService> {
        private OkHttpClient okHttpClient;

        private Builder setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        private Builder() {
        }

        @Override
        protected OkHttpSastLinkService.Builder self() {
            return this;
        }

        @Override
        public OkHttpSastLinkService build() {
            //检查参数
            if (this.redirect_uri.isEmpty() || this.client_id.isEmpty() || this.client_secret.isEmpty()) {
                throw new SastLinkException("redirect_uri, client_id or client_secret not exist");
            }
            if (this.host_name.isEmpty()) {
                throw new SastLinkException("sast-link server host_name is needed in building a sastLinkService");
            }
            if (this.code_verifier.isEmpty()) {
                throw new SastLinkException("code_verifier is needed in building a sastLinkService");
            }
            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient();
            }
            return new OkHttpSastLinkService(this);
        }
    }

}
