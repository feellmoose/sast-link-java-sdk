package sast.sastlink.sdk.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.*;
import sast.sastlink.sdk.enums.GrantType;
import sast.sastlink.sdk.enums.SastLinkApi;
import sast.sastlink.sdk.exception.SastLinkException;
import sast.sastlink.sdk.exception.errors.SastLinkErrorEnum;
import sast.sastlink.sdk.model.UserInfo;
import sast.sastlink.sdk.model.response.AccessTokenData;
import sast.sastlink.sdk.model.response.RefreshResponse;
import sast.sastlink.sdk.service.AbstractSastLinkService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

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
    public AccessTokenData accessToken(String code) throws SastLinkException {
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
                .method("POST",RequestBody.create("".getBytes()))
                .build();
        String body;
        try {
            ResponseBody responseBody= okHttpClient.newCall(request).execute().body();
            if(responseBody == null){
                throw new SastLinkException(SastLinkErrorEnum.NULL_RESPONSE_BODY);
            }
            body = responseBody.string();
            if(body.isEmpty()){
                throw new SastLinkException(SastLinkErrorEnum.EMPTY_RESPONSE_BODY);
            }
        } catch (IOException e) {
            throw new SastLinkException(SastLinkErrorEnum.IO_ERROR,e);
        }





        return null;
    }

    @Override
    public RefreshResponse refresh(String refresh_token) throws SastLinkException {
        return null;
    }

    @Override
    public UserInfo userInfo(String accessToken) throws SastLinkException {
        return null;
    }

    public static class Builder extends AbstractSastLinkService.Builder<OkHttpSastLinkService> {
        private OkHttpClient okHttpClient;
        private Builder setOkHttpClient(OkHttpClient okHttpClient){
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
            if(okHttpClient == null){
                okHttpClient = new OkHttpClient();
            }
            return new OkHttpSastLinkService(this);
        }
    }

}
