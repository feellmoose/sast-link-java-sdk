package sast.sastlink.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @projectName: sast-link-SDK
 * @author: feelMoose
 * @date: 2023/7/16 21:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private String userId;
    private String email;
    private String avatar;
    private Badge badge;
    private String bio;
    private String dep;
    private List<String> hide;
    private List<String> link;
    private String nickname;
    private String org;
}
