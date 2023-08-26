package sast.sastlink.sdk.enums;

import java.net.URI;
import java.net.URISyntaxException;

public enum SastLinkApi {
    AUTHORIZE("OAUTH认证", "/oauth2/authorize"),
    ACCESS_TOKEN("获取ACCESS_TOKEN", "/oauth2/token"),
    REFRESH("刷新ACCESS_TOKEN", "/oauth2/refresh"),
    USER_INFO("获取用户信息", "/oauth2/userinfo"),
    VERIFY_ACCOUNT("验证账号", "/verify/account?username={username}&flag={flag}"),
    LOGIN("登录", "/user/login"),
    REGISTER("注册", "/user/register"),
    SEND_EMAIL("发送邮箱", "/sendEmail"),
    VERIFY_CAPTCHA("验证验证码", "/verify/captcha"),
    LOGOUT("用户登出", "/user/logout");

    SastLinkApi(String description, String url) {
        this.description = description;
        this.url = url;
    }

    private final String description;
    private final String url;

    public String getDescription() {
        return description;
    }

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
