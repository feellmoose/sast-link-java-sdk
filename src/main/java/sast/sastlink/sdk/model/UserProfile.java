package sast.sastlink.sdk.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

/**
 * @projectName: sast-link-SDK
 * @author: feelMoose
 * @date: 2023/8/19 15:12
 */
public class UserProfile {
    private String nickname;
    private String avatar;
    @JsonAlias("dep")
    private String dep;
    @JsonAlias("org")
    private Integer organization;
    private String email;
    @JsonAlias("bio")
    private String biography;
    private List<String> link;
    private Badge badge;
    private List<String> hide;
    @JsonAlias("carrerRecord")
    private List<CareerRecord> careerRecord;

    public UserProfile() {
    }

    public UserProfile(String nickname, String avatar, String dep, Integer organization, String email, String biography, List<String> link, Badge badge, List<String> hide, List<CareerRecord> careerRecord) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.dep = dep;
        this.organization = organization;
        this.email = email;
        this.biography = biography;
        this.link = link;
        this.badge = badge;
        this.hide = hide;
        this.careerRecord = careerRecord;
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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public Integer getOrganization() {
        return organization;
    }

    public void setOrganization(Integer organization) {
        this.organization = organization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<String> getLink() {
        return link;
    }

    public void setLink(List<String> link) {
        this.link = link;
    }

    public Badge getBadge() {
        return badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public List<String> getHide() {
        return hide;
    }

    public void setHide(List<String> hide) {
        this.hide = hide;
    }

    public List<CareerRecord> getCareerRecord() {
        return careerRecord;
    }

    public void setCareerRecord(List<CareerRecord> careerRecord) {
        this.careerRecord = careerRecord;
    }
}
