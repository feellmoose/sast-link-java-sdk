package fun.feellmoose.test;

import com.fasterxml.jackson.core.type.TypeReference;
import fun.feellmoose.constants.LinkParamConstants;
import fun.feellmoose.enums.SastLinkErrorEnum;
import fun.feellmoose.enums.Scope;
import fun.feellmoose.model.response.data.AccessToken;
import fun.feellmoose.model.response.data.RefreshToken;
import fun.feellmoose.model.response.data.User;
import fun.feellmoose.service.SastLinkService;
import fun.feellmoose.service.impl.HttpClientSastLinkService;
import fun.feellmoose.test.data.Verify;
import lombok.Setter;
import lombok.experimental.Accessors;
import fun.feellmoose.enums.SastLinkApi;
import fun.feellmoose.enums.State;
import fun.feellmoose.exception.SastLinkException;
import fun.feellmoose.model.response.SastLinkResponse;
import fun.feellmoose.test.data.Token;
import fun.feellmoose.util.JsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static fun.feellmoose.service.impl.HttpClientSastLinkService.boundary;


public class TestSastLinkServiceAdapter implements SastLinkService {
    private HttpClientSastLinkService sastLinkService;
    private HttpClient httpClient;
    private final String clientId;
    private final String clientSecret;
    private final String codeVerifier;
    private final String redirectUri;
    private final String hostName;

    public TestSastLinkServiceAdapter(Builder builder) {
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.codeVerifier = builder.codeVerifier;
        this.redirectUri = builder.redirectUri;
        this.hostName = builder.hostName;
    }

    @Setter
    @Accessors(chain = true)
    public static class Builder {
        private HttpClient httpClient;
        private String clientId;
        private String clientSecret;
        private String codeVerifier;
        private String redirectUri;
        private String hostName;

        public TestSastLinkServiceAdapter build() {
            if (this.httpClient == null) {
                this.httpClient = HttpClient.newHttpClient();
            }
            TestSastLinkServiceAdapter testSastLinkService = new TestSastLinkServiceAdapter(this);
            SastLinkService linkService = new HttpClientSastLinkService.Builder()
                    .setHttpClient(this.httpClient)
                    .setClientId(this.clientId)
                    .setClientSecret(this.clientSecret)
                    .setCodeVerifier(this.codeVerifier)
                    .setRedirectUri(this.redirectUri)
                    .setHostName(this.hostName)
                    .build();
            testSastLinkService.httpClient = this.httpClient;
            testSastLinkService.sastLinkService = (HttpClientSastLinkService) linkService;
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
        HttpRequest request = HttpRequest.newBuilder(uri)
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
        Map<Object, Object> data = Map.of("password", password);
        HttpRequest request = HttpRequest.newBuilder(SastLinkApi.LOGIN.getHttpURI(hostName))
                .header(LinkParamConstants.CONTENT_TYPE, "multipart/form-data;boundary=" + boundary)
                .header("LOGIN-TICKET", loginTicket)
                .POST(HttpClientSastLinkService.ofMimeMultipartData(data))
                .build();
        Token token;
        try {
            token = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
                    .body()
                    .transform(body -> {
                        if (body.isEmpty()) throw new SastLinkException(SastLinkErrorEnum.EMPTY_RESPONSE_BODY);
                        SastLinkResponse<Token> response = JsonUtil.fromJson(body, new TypeReference<>() {
                        });
                        if (!response.isSuccess()) throw new SastLinkException(response);
                        return response.getData();
                    });
        } catch (IOException e) {
            throw new SastLinkException(SastLinkErrorEnum.IO_ERROR, e);
        } catch (InterruptedException e) {
            throw new SastLinkException(e);
        }
        return token;
    }

    private Verify verifyAccount(String username) throws SastLinkException {
        URI uri;
        try {
            uri = new URI(SastLinkApi.VERIFY_ACCOUNT.getHttp(hostName) +
                    "?username=" + username +
                    "&flag=1");
        } catch (URISyntaxException e) {
            throw new SastLinkException(e);
        }
        HttpRequest request = HttpRequest.newBuilder(uri)
                .GET()
                .build();
        Verify verify;
        try {
            verify = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
                    .body()
                    .transform(body -> {
                        if (body.isEmpty()) throw new SastLinkException(SastLinkErrorEnum.EMPTY_RESPONSE_BODY);
                        SastLinkResponse<Verify> response = JsonUtil.fromJson(body, new TypeReference<>() {
                        });
                        if (!response.isSuccess()) throw new SastLinkException(response);
                        return response.getData();
                    });
        } catch (IOException e) {
            throw new SastLinkException(SastLinkErrorEnum.IO_ERROR, e);
        } catch (InterruptedException e) {
            throw new SastLinkException(e);
        }
        return verify;
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
