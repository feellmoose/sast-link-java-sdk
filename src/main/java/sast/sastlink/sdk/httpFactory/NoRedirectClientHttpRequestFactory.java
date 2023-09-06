package sast.sastlink.sdk.httpFactory;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.IOException;
import java.net.HttpURLConnection;

public class NoRedirectClientHttpRequestFactory extends
        SimpleClientHttpRequestFactory {
    @Override
    protected void prepareConnection(HttpURLConnection connection,
                                     String httpMethod) throws IOException {
        super.prepareConnection(connection, httpMethod);
        connection.setInstanceFollowRedirects(false);
    }
}