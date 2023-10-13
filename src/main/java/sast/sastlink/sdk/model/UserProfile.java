package sast.sastlink.sdk.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import sast.sastlink.sdk.enums.Organization;

import java.util.List;

/**
 * @projectName: sast-link-SDK
 * @author: feelMoose
 * @date: 2023/8/19 15:12
 */
public class UserProfile {
    private String nickname;
    private String avatar;
    private String dep;
    private Organization organization;
    private String email;
    private String biography;
    private List<String> link;
    private Badge badge;
    private List<String> hide;
    private List<CareerRecord> careerRecord;

    public UserProfile(UserInfo userInfo) {
        this.nickname = userInfo.getNickname();
        this.avatar = userInfo.getAvatar();
        this.dep = userInfo.getDep();
        this.organization = Organization.valueOf(userInfo.getOrg());
        this.email = userInfo.getEmail();
        this.biography = userInfo.getBio();
        this.link = getLinkFromJson(userInfo.getLink());
        this.badge = getBadgeFromJson(userInfo.getBadge());
        this.hide = getHideFromJson(userInfo.getHide());
        this.careerRecord = getCareerRecordFromJson(null);
    }
    private List<String> getLinkFromJson(String linkJson){
        //todo
        return null;
    }
    private Badge getBadgeFromJson(String badgeJson){
        //todo
        return null;
    }

    List<String> getHideFromJson(String hideJson){
        //todo
        return null;
    }

    List<CareerRecord> getCareerRecordFromJson(String careerRecord){
        return null;
    }


    @Override
    public String toString() {
        return "UserProfile{" +
                "nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", dep='" + dep + '\'' +
                ", organization=" + organization +
                ", email='" + email + '\'' +
                ", biography='" + biography + '\'' +
                ", link=" + link +
                ", badge=" + badge +
                ", hide=" + hide +
                ", careerRecord=" + careerRecord +
                '}';
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDep() {
        return dep;
    }

    public Organization getOrganization() {
        return organization;
    }

    public String getEmail() {
        return email;
    }

    public String getBiography() {
        return biography;
    }

    public List<String> getLink() {
        return link;
    }

    public Badge getBadge() {
        return badge;
    }

    public List<String> getHide() {
        return hide;
    }

    public List<CareerRecord> getCareerRecord() {
        return careerRecord;
    }
}
