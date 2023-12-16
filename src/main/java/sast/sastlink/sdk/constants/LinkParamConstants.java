package sast.sastlink.sdk.constants;

import java.math.BigInteger;
import java.util.Random;

public interface LinkParamConstants {
    String boundary = new BigInteger(256, new Random()).toString();
    String CODE = "code";
    String CODE_VERIFIER = "code_verifier";
    String GRANT_TYPE = "grant_type";
    String REDIRECT_URI = "redirect_uri";
    String CLIENT_ID = "client_id";
    String CLIENT_SECRET = "client_secret";
    String REFRESH_TOKEN = "refresh_token";
    String AUTHORIZATION = "Authorization";
    String CONTENT_TYPE = "Content-Type";
}
