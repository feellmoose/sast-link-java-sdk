package sast.sastlink.sdk.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sast.sastlink.sdk.enums.GrantType;
import sast.sastlink.sdk.enums.SastLinkApi;
import sast.sastlink.sdk.exception.SastLinkException;
import sast.sastlink.sdk.enums.SastLinkErrorEnum;
import sast.sastlink.sdk.model.response.SastLinkResponse;
import sast.sastlink.sdk.model.response.data.AccessToken;
import sast.sastlink.sdk.model.response.data.RefreshToken;
import sast.sastlink.sdk.model.response.data.User;
import sast.sastlink.sdk.util.JsonUtil;


public final class RestTemplateSastLinkService extends AbstractSastLinkService<RestTemplateSastLinkService> {
    private final RestTemplate restTemplate;

    public static RestTemplateSastLinkService.Builder builder() {
        return new RestTemplateSastLinkService.Builder();
    }

    private RestTemplateSastLinkService(Builder builder) {
        super(builder);
        this.restTemplate = builder.restTemplate;
    }

    @Override
    public AccessToken accessToken(String code) throws SastLinkException {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(CODE, code);
        map.add(CODE_VERIFIER, code_verifier);
        map.add(GRANT_TYPE, GrantType.AUTHORIZATION_CODE.name);
        map.add(REDIRECT_URI, redirect_uri);
        map.add(CLIENT_ID, client_id);
        map.add(CLIENT_SECRET, client_secret);
        RequestEntity<MultiValueMap<String, String>> entity = RequestEntity
                .post(SastLinkApi.ACCESS_TOKEN.getHttp(host_name))
                .header(CONTENT_TYPE, "multipart/form-data")
                .body(map);
        String body = restTemplate.exchange(entity, String.class).getBody();
        if (body == null) throw new SastLinkException(SastLinkErrorEnum.NULL_RESPONSE_BODY);
        if (body.isEmpty()) throw new SastLinkException(SastLinkErrorEnum.EMPTY_RESPONSE_BODY);
        SastLinkResponse<AccessToken> response = JsonUtil.fromJson(body, new TypeReference<>() {
        });
        if (!response.isSuccess()) {
            throw new SastLinkException(response);
        }
        return response.getData();
    }


    @Override
    public RefreshToken refreshToken(String refreshToken) throws SastLinkException {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(REFRESH_TOKEN, refreshToken);
        map.add(GRANT_TYPE, GrantType.REFRESH_TOKEN.name);
        RequestEntity<MultiValueMap<String, String>> entity = RequestEntity
                .post(SastLinkApi.REFRESH.getHttp(host_name))
                .header(CONTENT_TYPE, "multipart/form-data")
                .body(map);
        String body = restTemplate.exchange(entity, String.class).getBody();
        if (body == null) throw new SastLinkException(SastLinkErrorEnum.NULL_RESPONSE_BODY);
        if (body.isEmpty()) throw new SastLinkException(SastLinkErrorEnum.EMPTY_RESPONSE_BODY);
        SastLinkResponse<RefreshToken> response = JsonUtil.fromJson(body, new TypeReference<>() {
        });
        if (!response.isSuccess()) {
            throw new SastLinkException(response);
        }
        return response.getData();
    }

    @Override
    public User user(String accessToken) throws SastLinkException {
        RequestEntity<Void> entity = RequestEntity
                .get(SastLinkApi.USER_INFO.getHttp(host_name))
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .build();
        String body = restTemplate.exchange(entity, String.class).getBody();
        if (body == null) throw new SastLinkException(SastLinkErrorEnum.NULL_RESPONSE_BODY);
        if (body.isEmpty()) throw new SastLinkException(SastLinkErrorEnum.EMPTY_RESPONSE_BODY);
        SastLinkResponse<User> response = JsonUtil.fromJson(body, new TypeReference<>() {
        });
        if (!response.isSuccess()) {
            throw new SastLinkException(response);
        }
        return response.getData();
    }


    public static class Builder extends AbstractSastLinkService.Builder<RestTemplateSastLinkService,Builder> {
        private RestTemplate restTemplate;

        public Builder setRestTemplate(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public RestTemplateSastLinkService build() {
            super.build();
            if (this.restTemplate == null) {
                this.restTemplate = new RestTemplate();
            }
            return new RestTemplateSastLinkService(this);
        }
    }
}
