package fun.feellmoose.enums;

/**
 * @projectName: sast-link-SDK
 * @author: feelMoose
 * @date: 2023/8/21 23:41
 */
public enum GrantType {
    AUTHORIZATION_CODE("authorization_code"),
    PASSWORD("password"),
    CLIENT_CREDENTIALS("client_credentials"),
    IMPLICIT("implicit"),
    TOKEN("token"),
    REFRESH_TOKEN("refresh_token");

    GrantType(String name) {
        this.name = name;
    }

    public final String name;

}
