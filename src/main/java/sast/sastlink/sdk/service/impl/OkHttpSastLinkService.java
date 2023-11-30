package sast.sastlink.sdk.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.*;
import sast.sastlink.sdk.enums.GrantType;
import sast.sastlink.sdk.enums.SastLinkApi;
import sast.sastlink.sdk.exception.SastLinkException;
import sast.sastlink.sdk.enums.SastLinkErrorEnum;
import sast.sastlink.sdk.model.response.SastLinkResponse;
import sast.sastlink.sdk.model.response.data.AccessToken;
import sast.sastlink.sdk.model.response.data.RefreshToken;
import sast.sastlink.sdk.model.response.data.User;
import sast.sastlink.sdk.util.JsonUtil;

import java.io.IOException;

public final class OkHttpSastLinkService extends AbstractSastLinkService<OkHttpSastLinkService> {
    private final OkHttpClient okHttpClient;

    public static OkHttpSastLinkService.Builder Builder() {
        return new OkHttpSastLinkService.Builder();
    }

    private OkHttpSastLinkService(Builder builder) {
        super(builder);
        this.okHttpClient = builder.okHttpClient;
    }

    @Override
    public AccessToken accessToken(String code) throws SastLinkException {
        HttpUrl url = HttpUrl.get(SastLinkApi.ACCESS_TOKEN.getHttpURI(host_name));
        FormBody formBody = new FormBody.Builder()
                .add(CODE, code)
                .add(CODE_VERIFIER, code_verifier)
                .add(GRANT_TYPE, GrantType.AUTHORIZATION_CODE.name)
                .add(REDIRECT_URI, redirect_uri)
                .add(CLIENT_ID, client_id)
                .add(CLIENT_SECRET, client_secret).build();
        Request request = new Request.Builder()
                .header(CONTENT_TYPE, "multipart/form-data")
                .url(url)
                .method("POST", formBody)
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
        HttpUrl url = HttpUrl.get(SastLinkApi.REFRESH.getHttpURI(host_name));
        FormBody formBody = new FormBody.Builder()
                .add(REFRESH_TOKEN, refreshToken)
                .add(GRANT_TYPE, GrantType.REFRESH_TOKEN.name).build();
        Request request = new Request.Builder()
                .header(CONTENT_TYPE, "multipart/form-data")
                .url(url)
                .method("POST", formBody)
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
        HttpUrl url = HttpUrl.get(SastLinkApi.USER_INFO.getHttpURI(host_name));
        Request request = new Request.Builder()
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .url(url)
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
            if (responseBody == null) throw new SastLinkException(SastLinkErrorEnum.NULL_RESPONSE_BODY);
            body = responseBody.string();
            if (body.isEmpty()) throw new SastLinkException(SastLinkErrorEnum.EMPTY_RESPONSE_BODY);
        } catch (IOException e) {
            throw new SastLinkException(SastLinkErrorEnum.IO_ERROR, e);
        }
        return body;
    }

    public static class Builder extends AbstractSastLinkService.Builder<OkHttpSastLinkService> {
        private OkHttpClient okHttpClient;

        public Builder setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        private Builder() {
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public OkHttpSastLinkService build() {
            super.build();
            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient();
            }
            return new OkHttpSastLinkService(this);
        }
    }

}
