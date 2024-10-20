package fun.feellmoose.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import fun.feellmoose.enums.GrantType;
import fun.feellmoose.enums.SastLinkApi;
import fun.feellmoose.enums.SastLinkErrorEnum;
import fun.feellmoose.exception.SastLinkException;
import fun.feellmoose.model.response.SastLinkResponse;
import fun.feellmoose.model.response.data.AccessToken;
import fun.feellmoose.model.response.data.RefreshToken;
import fun.feellmoose.model.response.data.User;
import fun.feellmoose.service.SastLinkService;
import fun.feellmoose.util.JsonUtil;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


public final class RestTemplateSastLinkService extends AbstractSastLinkService {
    private final RestTemplate restTemplate;

    private RestTemplateSastLinkService(Builder builder) {
        super(builder);
        this.restTemplate = builder.restTemplate;
    }

    @Override
    public AccessToken accessToken(String code) throws SastLinkException {
        if (code_verifier == null) throw new SastLinkException("Get AccessToken by params(code) with no default verifier is not allowed");
        if (redirect_uri == null) throw new SastLinkException("Get AccessToken by params(code) with no default verifier is not allowed");
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
    public AccessToken accessToken(String code, String redirectURI, String codeVerifier) throws SastLinkException {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (codeVerifier != null) map.add(CODE_VERIFIER, codeVerifier);
        if (redirectURI != null) map.add(REDIRECT_URI, redirectURI);
        else if (redirect_uri != null) map.add(REDIRECT_URI, redirect_uri);
        else throw new SastLinkException("Get AccessToken by params(code) with no default verifier is not allowed");
        map.add(CODE, code);
        map.add(GRANT_TYPE, GrantType.AUTHORIZATION_CODE.name);
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


    public static class Builder extends AbstractSastLinkService.Builder<Builder> {
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
        public SastLinkService build() {
            super.build();
            if (this.restTemplate == null) {
                this.restTemplate = new RestTemplate();
            }
            return new RestTemplateSastLinkService(this);
        }
    }
}
