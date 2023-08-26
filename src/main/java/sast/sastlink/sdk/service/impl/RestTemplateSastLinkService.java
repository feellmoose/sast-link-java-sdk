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
import sast.sastlink.sdk.model.UserInfo;
import sast.sastlink.sdk.model.response.AccessTokenResponse;
import sast.sastlink.sdk.model.response.CommonResponse;
import sast.sastlink.sdk.model.response.RefreshResponse;
import sast.sastlink.sdk.service.SastLinkService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * @projectName: sast-link-SDK
 * @author: feelMoose
 * @date: 2023/7/16 16:02
 */

public class RestTemplateSastLinkService extends AbstractSastLinkService {
    private final RestTemplate restTemplate;
    private static final String authorize_response_type = "code";

    public static Builder Builder() {
        return new Builder();
    }

    @Override
    public AccessTokenResponse accessToken(String code) throws SastLinkException {
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
        ResponseEntity<AccessTokenResponse> response = restTemplate.exchange(request, AccessTokenResponse.class);
        return response.getBody();
    }


    @Override
    public String authorize(String token, String code_challenge, String code_challenge_method) throws SastLinkException {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("token", token);
        URI uri;
        try {
            uri = new URI(SastLinkApi.AUTHORIZE.getHttp(host_name) +
                    "?client_id=" + this.client_id +
                    "&code_challenge=" + code_challenge +
                    "&code_challenge_method=" + code_challenge_method +
                    "&redirect_uri=" + this.redirect_uri +
                    "&response_type=" + authorize_response_type +
                    "&scope=" + Scope.ALL.name +
                    "&state=" + State.XYZ.name);
        } catch (URISyntaxException e) {
            throw new SastLinkException(e.getMessage());
        }
        URI redirect_uri = Optional.ofNullable(restTemplate.postForLocation(uri, multiValueMap))
                .orElseThrow(() -> new SastLinkException("error  uri,final uri value is null"));
        String queryString = redirect_uri.getQuery();
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
        ResponseEntity<RefreshResponse> response = restTemplate.exchange(request, RefreshResponse.class);
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
        Map<String, String> resultMap = Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new SastLinkException("Error get userInfo by accessToken,return value is null."))
                .getData();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(resultMap.get("user_id"));
        userInfo.setWechatId(resultMap.get("wechat_id"));
        userInfo.setEmail(resultMap.get("email"));
        return userInfo;
    }

    @Override
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
        return Optional.ofNullable(commonResponse.getData().get("token"))
                .orElseThrow(() -> new SastLinkException("error, no token in response.\nerror response value: " + commonResponse));
    }

    @Override
    public String sendCaptcha(String email) throws SastLinkException {
        String registerTicket = verifyAccount(email, "0");
        /* 发送邮箱验证码 */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("REGISTER-TICKET", registerTicket);
        RequestEntity<?> request = new RequestEntity<>(
                httpHeaders,
                HttpMethod.GET,
                SastLinkApi.SEND_EMAIL.getHttpURI(host_name));
        ResponseEntity<CommonResponse> responseEntity;
        try {
            responseEntity = restTemplate.exchange(request, CommonResponse.class);
        } catch (RestClientException e) {
            throw new SastLinkException(e.getMessage());
        }
        Optional.ofNullable(responseEntity.getBody())
                .orElseThrow(() -> new SastLinkException("error, send captcha response is null."));
        return registerTicket;
    }

    @Override
    public boolean checkCaptchaAndRegister(String captcha, String registerTicket, String password) throws SastLinkException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add("REGISTER-TICKET", registerTicket);
        /* 验证邮箱验证码 */
        MultiValueMap<String, String> checkCaptchaMultiValueMap = new LinkedMultiValueMap<>();
        checkCaptchaMultiValueMap.add("captcha", captcha);
        RequestEntity<MultiValueMap<String, String>> checkCaptchaRequest = new RequestEntity<>(
                checkCaptchaMultiValueMap,
                httpHeaders,
                HttpMethod.POST,
                SastLinkApi.VERIFY_CAPTCHA.getHttpURI(host_name));
        ResponseEntity<CommonResponse> checkCaptchaResponseEntity;
        try {
            checkCaptchaResponseEntity = restTemplate.exchange(checkCaptchaRequest, CommonResponse.class);
        } catch (RestClientException e) {
            throw new SastLinkException(e.getMessage());
        }
        CommonResponse response = Optional.ofNullable(checkCaptchaResponseEntity.getBody())
                .orElseThrow(() -> new SastLinkException("error, check captcha response is null."));
        if (!response.getSuccess()) {
            throw new SastLinkException("check captcha error.");
        }
        /* 正式注册 */
        MultiValueMap<String, String> registerMultiValueMap = new LinkedMultiValueMap<>();
        registerMultiValueMap.add("password", password);
        RequestEntity<MultiValueMap<String, String>> registerRequest = new RequestEntity<>(
                registerMultiValueMap,
                httpHeaders,
                HttpMethod.POST,
                SastLinkApi.REGISTER.getHttpURI(host_name));
        ResponseEntity<CommonResponse> registerResponseEntity;
        try {
            registerResponseEntity = restTemplate.exchange(registerRequest, CommonResponse.class);
        } catch (RestClientException e) {
            throw new SastLinkException(e.getMessage());
        }
        CommonResponse commonResponse = Optional.ofNullable(registerResponseEntity.getBody())
                .orElseThrow(() -> new SastLinkException("error, register response is null."));
        return commonResponse.getSuccess();
    }

    @Override
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
            case "0" -> Optional.ofNullable(commonResponse.getData().get("register_ticket"))
                    .orElseThrow(() -> new SastLinkException("error, no register ticket in response.\nerror response value: " + commonResponse));
            case "1" -> Optional.ofNullable(commonResponse.getData().get("login_ticket"))
                    .orElseThrow(() -> new SastLinkException("error, no register ticket in response.\nerror response value: " + commonResponse));
            default -> throw new SastLinkException("invalid flag value.");
        };
    }


    private static RestTemplate getDefaultRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return new RestTemplate(factory);
    }

    private RestTemplateSastLinkService(Builder builder) {
        super(builder);
        restTemplate = builder.restTemplate;
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
            if (this.redirect_uri.isEmpty() || this.client_id.isEmpty() || this.client_secret.isEmpty()) {
                throw new SastLinkException("redirect_uri, client_id or client_secret not exist");
            }
            if (this.host_name.isEmpty()) {
                throw new SastLinkException("sast-link server host_name is needed in building a sastLinkService");
            }
            if (this.code_verifier.isEmpty()) {
                throw new SastLinkException("code_verifier is needed in building a sastLinkService");
            }
            if (restTemplate == null) {
                this.setRestTemplate(RestTemplateSastLinkService.getDefaultRestTemplate());
            }
            return new RestTemplateSastLinkService(this);
        }
    }
}
