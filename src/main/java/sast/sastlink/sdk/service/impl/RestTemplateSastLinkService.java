package sast.sastlink.sdk.service.impl;


import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sast.sastlink.sdk.enums.GrantType;
import sast.sastlink.sdk.enums.SastLinkApi;
import sast.sastlink.sdk.enums.Scope;
import sast.sastlink.sdk.enums.State;
import sast.sastlink.sdk.exception.SastLinkException;
import sast.sastlink.sdk.httpFactory.NoRedirectClientHttpRequestFactory;
import sast.sastlink.sdk.model.Badge;
import sast.sastlink.sdk.model.UserInfo;
import sast.sastlink.sdk.model.response.AccessTokenData;
import sast.sastlink.sdk.model.response.CommonResponse;
import sast.sastlink.sdk.model.response.RefreshResponse;
import sast.sastlink.sdk.service.AbstractSastLinkService;
import sast.sastlink.sdk.service.SastLinkService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


/**
 * @projectName: sast-link-SDK
 * @author: feelMoose
 * @date: 2023/7/16 16:02
 */

public class RestTemplateSastLinkService extends AbstractSastLinkService<RestTemplateSastLinkService> {
    private RestTemplate restTemplate;

    public static Builder Builder() {
        return new Builder();
    }

    @Override
    public AccessTokenData accessToken(String code) throws SastLinkException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("code", code);
        multiValueMap.add("code_verifier", this.code_verifier);
        multiValueMap.add("grant_type", GrantType.AUTHORIZATION_CODE.name);
        multiValueMap.add("redirect_uri", this.redirect_uri);
        multiValueMap.add("client_id", this.client_id);
        multiValueMap.add("client_secret", this.client_secret);
        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(
                multiValueMap,
                httpHeaders,
                HttpMethod.POST,
                SastLinkApi.ACCESS_TOKEN.getHttpURI(host_name));
        ResponseEntity<CommonResponse> response;
        try {
            response = restTemplate.exchange(request, CommonResponse.class);
        } catch (RestClientException e) {
            throw new SastLinkException(e.getMessage());
        }
        CommonResponse commonResponse = Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new SastLinkException("Error get userInfo by accessToken, return value is null."));
        if (!commonResponse.getSuccess()) {
            throw new SastLinkException("Error get userInfo: " + commonResponse);
        }
        Map<String, Object> resultMap = Optional.ofNullable(commonResponse.getData()).orElse(Collections.emptyMap());
        return new AccessTokenData((String) resultMap.get("access_token"),
                (Integer) resultMap.get("expires_in"),
                (String) resultMap.get("refresh_token"),
                (String) resultMap.get("scope"),
                (String) resultMap.get("token_type"));
    }


    public String authorize(String token, String code_challenge, String code_challenge_method) throws SastLinkException {
        URI uri;
        try {
            uri = new URI(SastLinkApi.AUTHORIZE.getHttp(host_name) +
                    "?client_id=" + this.client_id +
                    "&code_challenge=" + code_challenge +
                    "&code_challenge_method=" + code_challenge_method +
                    "&redirect_uri=" + this.redirect_uri +
                    "&response_type=" + authorize_response_type +
                    "&scope=" + Scope.ALL.name +
                    "&state=" + State.XYZ.name +
                    "&part=" + token);
        } catch (URISyntaxException e) {
            throw new SastLinkException(e.getMessage());
        }
        HttpHeaders headers = restTemplate.execute(uri, HttpMethod.GET, restTemplate.httpEntityCallback(null), HttpMessage::getHeaders);
        URI redirect_uri;
        try {
            redirect_uri = Optional.ofNullable(headers == null ? null : headers.getLocation())
                    .orElseThrow(() -> new SastLinkException("error uri, final uri value is null"));
        } catch (Exception e) {
            throw new SastLinkException(e.getMessage());
        }
        String queryString = redirect_uri.getQuery();
        if (queryString.contains("error")) {
            throw new SastLinkException("sast-link error: " + queryString);
        }
        return queryString.substring(queryString.indexOf("code=") + 5, queryString.indexOf("&state="));
    }

    @Override
    public RefreshResponse refresh(String refresh_token) throws SastLinkException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("refresh_token", refresh_token);
        multiValueMap.add("grant_type", GrantType.REFRESH_TOKEN.name);
        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(
                multiValueMap,
                httpHeaders,
                HttpMethod.POST,
                SastLinkApi.REFRESH.getHttpURI(host_name));
        ResponseEntity<RefreshResponse> response;
        try {
            response = restTemplate.exchange(request, RefreshResponse.class);
        } catch (RestClientException e) {
            throw new SastLinkException(e.getMessage());
        }
        return response.getBody();
    }

    @Override
    public UserInfo userInfo(String accessToken) throws SastLinkException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);
        RequestEntity<?> request = new RequestEntity<>(
                httpHeaders,
                HttpMethod.GET,
                SastLinkApi.USER_INFO.getHttpURI(host_name));
        ResponseEntity<CommonResponse> response;
        try {
            response = restTemplate.exchange(request, CommonResponse.class);
        } catch (RestClientException e) {
            throw new SastLinkException(e.getMessage());
        }
        CommonResponse commonResponse = Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new SastLinkException("Error get userInfo by accessToken, return value is null."));
        if (!commonResponse.getSuccess()) {
            throw new SastLinkException("Error get userInfo: " + commonResponse);
        }
        Map<String, Object> resultMap = Optional.ofNullable(commonResponse.getData()).orElse(Collections.emptyMap());
        return new UserInfo()
                .setHide((List<String>) resultMap.get("hide"))
                .setUserId((String) resultMap.get("userId"))
                .setLink((List<String>) resultMap.get("link"))
                .setAvatar((String) resultMap.get("avatar"))
                .setBio((String) resultMap.get("bio"))
                .setDep((String) resultMap.get("dep"))
                .setBadge((Badge) resultMap.get("badge"))
                .setEmail((String) resultMap.get("email"))
                .setNickname((String) resultMap.get("nickname"))
                .setOrg((String) resultMap.get("org"));
    }



    public String login(String email, String password) throws SastLinkException {
        String loginTicket = verifyAccount(email, "1");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("LOGIN-TICKET", loginTicket);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("password", password);
        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(
                multiValueMap,
                httpHeaders,
                HttpMethod.POST,
                SastLinkApi.LOGIN.getHttpURI(host_name));
        ResponseEntity<CommonResponse> responseEntity;
        try {
            responseEntity = restTemplate.exchange(request, CommonResponse.class);
        } catch (RestClientException e) {
            throw new SastLinkException(e.getMessage());
        }
        CommonResponse commonResponse = Optional.ofNullable(responseEntity.getBody())
                .orElseThrow(() -> new SastLinkException("error, login response is null."));
        return Optional.ofNullable((String) commonResponse.getData().get("loginToken"))
                .orElseThrow(() -> new SastLinkException("error, no loginToken in response.\nerror response value: " + commonResponse));
    }


    public boolean logout(String token) throws SastLinkException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("TOKEN", token);
        RequestEntity<?> request = new RequestEntity<>(
                httpHeaders,
                HttpMethod.POST,
                SastLinkApi.LOGOUT.getHttpURI(host_name)
        );
        ResponseEntity<CommonResponse> responseEntity;
        try {
            responseEntity = restTemplate.exchange(request, CommonResponse.class);
        } catch (RestClientException e) {
            throw new SastLinkException(e.getMessage());
        }
        CommonResponse commonResponse = Optional.ofNullable(responseEntity.getBody())
                .orElseThrow(() -> new SastLinkException("error, logout response is null."));
        return commonResponse.getSuccess();
    }

    private String verifyAccount(String userName, String flag) throws SastLinkException {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("username", userName);
        uriVariables.put("flag", flag);
        CommonResponse commonResponse;
        try {
            commonResponse = restTemplate.getForObject(SastLinkApi.VERIFY_ACCOUNT.getHttp(host_name), CommonResponse.class, uriVariables);
        } catch (RestClientException e) {
            throw new SastLinkException(e.getMessage());
        }
        if (commonResponse == null || !commonResponse.getSuccess()) {
            throw new SastLinkException("error response value: " + commonResponse);
        }
        return switch (flag) {
            case "0" -> Optional.ofNullable((String) commonResponse.getData().get("registerTicket"))
                    .orElseThrow(() -> new SastLinkException("error, no register ticket in response.\nerror response value: " + commonResponse));
            case "1" -> Optional.ofNullable((String) commonResponse.getData().get("loginTicket"))
                    .orElseThrow(() -> new SastLinkException("error, no register ticket in response.\nerror response value: " + commonResponse));
            default -> throw new SastLinkException("invalid flag value.");
        };
    }

    private RestTemplateSastLinkService(Builder builder) {
        super(builder);
        this.restTemplate = builder.restTemplate;
    }


    private void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static class Builder extends AbstractSastLinkService.Builder<RestTemplateSastLinkService> {
        private Builder() {
        }

        private RestTemplate restTemplate;

        @Override
        protected Builder self() {
            return this;
        }

        private static RestTemplate getDefaultRestTemplate() {
            SimpleClientHttpRequestFactory factory = new NoRedirectClientHttpRequestFactory();
            factory.setReadTimeout(5000);
            factory.setConnectTimeout(5000);
            return new RestTemplate(factory);
        }

        @Override
        public RestTemplateSastLinkService build() {
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
            if (this.restTemplate == null) {
                this.restTemplate = getDefaultRestTemplate();
            }
            return new RestTemplateSastLinkService(this);
        }
    }
}
