package sast.sastlink.sdk.model;

/**
 * @projectName: sast-link-SDK
 * @author: feelMoose
 * @date: 2023/7/16 21:35
 */
public class UserInfo {
    private String userId;
    private String wechatId;
    private String email;

    public UserInfo() {
    }

    public UserInfo(String userId, String wechatId, String email) {
        this.userId = userId;
        this.wechatId = wechatId;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                ", wechatId='" + wechatId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
