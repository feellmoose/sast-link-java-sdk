package sast.sastlink.sdk.test;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sast.sastlink.sdk.enums.SastLinkApi;
import sast.sastlink.sdk.enums.SastLinkErrorEnum;
import sast.sastlink.sdk.enums.Scope;
import sast.sastlink.sdk.enums.State;
import sast.sastlink.sdk.exception.SastLinkException;
import sast.sastlink.sdk.model.response.SastLinkResponse;
import sast.sastlink.sdk.model.response.data.AccessToken;
import sast.sastlink.sdk.model.response.data.RefreshToken;
import sast.sastlink.sdk.model.response.data.User;
import sast.sastlink.sdk.service.SastLinkService;
import sast.sastlink.sdk.service.impl.RestTemplateSastLinkService;
import sast.sastlink.sdk.test.data.Token;
import sast.sastlink.sdk.test.data.Verify;
import sast.sastlink.sdk.util.JsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;


public class RestTemplateTestSastLinkService implements SastLinkService {
    private RestTemplateSastLinkService sastLinkService;
    private RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;
    private final String codeVerifier;
    private final String redirectUri;
    private final String hostName;

    public RestTemplateTestSastLinkService(Builder builder) {
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.codeVerifier = builder.codeVerifier;
        this.redirectUri = builder.redirectUri;
        this.hostName = builder.hostName;
    }

    @Setter
    @Accessors(chain = true)
    public static class Builder {
        private RestTemplate restTemplate;
        private String clientId;
        private String clientSecret;
        private String codeVerifier;
        private String redirectUri;
        private String hostName;

        public RestTemplateTestSastLinkService build() {
            if (this.restTemplate == null) {
                this.restTemplate = new RestTemplate();
            }
            RestTemplateTestSastLinkService testSastLinkService = new RestTemplateTestSastLinkService(this);
            SastLinkService linkService = new RestTemplateSastLinkService.Builder()
                    .setRestTemplate(this.restTemplate)
                    .setClientId(this.clientId)
                    .setClientSecret(this.clientSecret)
                    .setCodeVerifier(this.codeVerifier)
                    .setRedirectUri(this.redirectUri)
                    .setHostName(this.hostName)
                    .build();
            testSastLinkService.restTemplate = this.restTemplate;
            testSastLinkService.sastLinkService = (RestTemplateSastLinkService) linkService;
            return testSastLinkService;
        }
    }


    public String authorize(String token, String code_challenge, String code_challenge_method) throws SastLinkException {
        URI uri;
        try {
            uri = new URI(SastLinkApi.AUTHORIZE.getHttp(hostName) +
                    "?client_id=" + this.clientId +
                    "&code_challenge=" + code_challenge +
                    "&code_challenge_method=" + code_challenge_method +
                    "&redirect_uri=" + this.redirectUri +
                    "&response_type=code&scope=" + Scope.ALL.name +
                    "&state=" + State.XYZ.name +
                    "&part=" + token);
        } catch (URISyntaxException e) {
            throw new SastLinkException(e);
        }
        HttpClient httpClient = HttpClient.newHttpClient();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder(uri)
                .GET().build();
        String location;
        try {
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            location = response.headers().firstValue("location").get();
        } catch (IOException | InterruptedException e) {
            throw new SastLinkException(SastLinkErrorEnum.IO_ERROR, e);
        }
        return location.substring(location.indexOf("code=") + 5, location.indexOf("&state="));
    }


    public Token login(String email, String password) throws SastLinkException {
        String loginTicket = verifyAccount(email).getLoginTicket();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("LOGIN-TICKET", loginTicket);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("password", password);
        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(
                multiValueMap,
                httpHeaders,
                HttpMethod.POST,
                SastLinkApi.LOGIN.getHttpURI(hostName));
        String body;
        try {
            body = restTemplate.exchange(request, String.class).getBody();
        } catch (RestClientException e) {
            throw new SastLinkException(e.getMessage());
        }
        if (body == null) throw new SastLinkException(SastLinkErrorEnum.NULL_RESPONSE_BODY);
        if (body.isEmpty()) throw new SastLinkException(SastLinkErrorEnum.EMPTY_RESPONSE_BODY);
        SastLinkResponse<Token> response = JsonUtil.fromJson(body, new TypeReference<>() {
        });
        if (!response.isSuccess()) {
            throw new SastLinkException(response);
        }
        return response.getData();
    }

    private Verify verifyAccount(String username) throws SastLinkException {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("username", username);
        uriVariables.put("flag", "1");
        String body;
        try {
            body = restTemplate.getForObject(SastLinkApi.VERIFY_ACCOUNT.getHttp(hostName), String.class, uriVariables);
        } catch (RestClientException e) {
            throw new SastLinkException(e.getMessage());
        }
        if (body == null) throw new SastLinkException(SastLinkErrorEnum.NULL_RESPONSE_BODY);
        if (body.isEmpty()) throw new SastLinkException(SastLinkErrorEnum.EMPTY_RESPONSE_BODY);
        SastLinkResponse<Verify> response = JsonUtil.fromJson(body, new TypeReference<>() {
        });
        if (!response.isSuccess()) {
            throw new SastLinkException(response);
        }
        return response.getData();

    }

    @Override
    public AccessToken accessToken(String code) throws SastLinkException {
        return sastLinkService.accessToken(code);
    }

    @Override
    public RefreshToken refreshToken(String refreshToken) throws SastLinkException {
        return sastLinkService.refreshToken(refreshToken);
    }

    @Override
    public User user(String accessToken) throws SastLinkException {
        return sastLinkService.user(accessToken);
    }
}
