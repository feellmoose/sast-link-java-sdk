package fun.feellmoose.enums;

import lombok.Getter;

import java.net.URI;
import java.net.URISyntaxException;

@Getter
public enum SastLinkApi {
    AUTHORIZE("OAUTH认证", "/oauth2/authorize"),
    VERIFY_ACCOUNT("验证账号", "/verify/account"),
    LOGIN("登录", "/user/login"),

    ACCESS_TOKEN("获取ACCESS_TOKEN", "/oauth2/token"),
    REFRESH("刷新ACCESS_TOKEN", "/oauth2/refresh"),
    USER_INFO("获取用户信息", "/oauth2/userinfo");
    SastLinkApi(String description, String url) {
        this.description = description;
        this.url = url;
    }
    private final String description;
    private final String url;
    public String getHttp(String hostName) {
        return "http://" + hostName + this.url;
    }

    public URI getHttpURI(String hostName) {
        try {
            return new URI(this.getHttp(hostName));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
