package fun.feellmoose.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import fun.feellmoose.enums.Organization;

import java.util.List;

/**
 * @projectName: sast-link-SDK
 * @author: feelMoose
 * @date: 2023/8/19 15:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public UserProfile(UserInfo userInfo) {
        this.nickname = userInfo.getNickname();
        this.avatar = userInfo.getAvatar();
        this.dep = userInfo.getDep();
        this.organization = getOrg(userInfo.getOrg());
        this.email = userInfo.getEmail();
        this.biography = userInfo.getBio();
        this.link = userInfo.getLink();
        this.badge = userInfo.getBadge();
        this.hide = userInfo.getHide();
    }

    private Organization getOrg(String org) {
        if (org == null || org.isEmpty()) {
            return null;
        }
        return Organization.getByStr(org);
    }
}
