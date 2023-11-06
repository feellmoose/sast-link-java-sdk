package sast.sastlink.sdk.model.response;

/**
 * @projectName: sast-link-SDK
 * @author: feelMoose
 * @date: 2023/8/19 21:23
 */
public class AccessTokenData {
    private String access_token;
    private long expires_in;
    private String refresh_token;
    private String scope;
    private String token_type;

    public AccessTokenData() {
    }

    public AccessTokenData(String access_token, long expires_in, String refresh_token, String scope, String token_type) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.scope = scope;
        this.token_type = token_type;
    }

    /* Getter and Setter */

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    @Override
    public String toString() {
        return "AccessTokenData{" +
                "access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", refresh_token='" + refresh_token + '\'' +
                ", scope='" + scope + '\'' +
                ", token_type='" + token_type + '\'' +
                '}';
    }
}
