package sast.sastlink.sdk.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import sast.sastlink.sdk.enums.GrantType;
import sast.sastlink.sdk.enums.SastLinkApi;
import sast.sastlink.sdk.exception.SastLinkException;
import sast.sastlink.sdk.enums.SastLinkErrorEnum;
import sast.sastlink.sdk.model.response.SastLinkResponse;
import sast.sastlink.sdk.model.response.data.AccessToken;
import sast.sastlink.sdk.model.response.data.RefreshToken;
import sast.sastlink.sdk.model.response.data.User;
import sast.sastlink.sdk.service.AbstractSastLinkService;
import sast.sastlink.sdk.util.JsonUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HttpClientSastLinkService extends AbstractSastLinkService<HttpClientSastLinkService> {
    private final HttpClient httpClient;
    private static final String boundary = new BigInteger(256, new Random()).toString();

    public static HttpClientSastLinkService.Builder Builder() {
        return new HttpClientSastLinkService.Builder();
    }

    protected HttpClientSastLinkService(Builder builder) {
        super(builder);
        this.httpClient = builder.httpClient;
    }

    @Override
    public AccessToken accessToken(String code) throws SastLinkException {
        Map<Object, Object> data = Map.of(
                CODE, code,
                CODE_VERIFIER, code_verifier,
                GRANT_TYPE, GrantType.AUTHORIZATION_CODE.name,
                REDIRECT_URI, this.redirect_uri,
                CLIENT_ID, this.client_id,
                CLIENT_SECRET, this.client_secret);
        HttpRequest request = HttpRequest.newBuilder(SastLinkApi.ACCESS_TOKEN.getHttpURI(host_name))
                .header(CONTENT_TYPE, "multipart/form-data;boundary=" + boundary)
                .POST(ofMimeMultipartData(data))
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
        Map<Object, Object> data = Map.of(
                REFRESH_TOKEN, refreshToken,
                GRANT_TYPE, GrantType.REFRESH_TOKEN.name);
        HttpRequest request = HttpRequest.newBuilder(SastLinkApi.REFRESH.getHttpURI(host_name))
                .header(CONTENT_TYPE, "multipart/form-data;boundary=" + boundary)
                .POST(ofMimeMultipartData(data))
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
                .header(AUTHORIZATION, "Bearer " + accessToken)
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

        public Builder setHttpClient(HttpClient httpClient) {
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


    private static HttpRequest.BodyPublisher ofMimeMultipartData(Map<Object, Object> data) {
        // Result request body
        List<byte[]> byteArrays = new ArrayList<>();
        // Separator with boundary
        byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=").getBytes(StandardCharsets.UTF_8);
        // Iterating over data parts
        try {
            for (Map.Entry<Object, Object> entry : data.entrySet()) {
                // Opening boundary
                byteArrays.add(separator);
                // If value is type of Path (file) append content type with file name and file binaries, otherwise simply append key=value
                if (entry.getValue() instanceof Path) {
                    var path = (Path) entry.getValue();
                    String mimeType = Files.probeContentType(path);
                    byteArrays.add(("\"" + entry.getKey() + "\"; filename=\"" + path.getFileName()
                            + "\"\r\nContent-Type: " + mimeType + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                    byteArrays.add(Files.readAllBytes(path));
                    byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));
                } else {
                    byteArrays.add(("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue() + "\r\n")
                            .getBytes(StandardCharsets.UTF_8));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Closing boundary
        byteArrays.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
        // Serializing as byte array
        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }
}
