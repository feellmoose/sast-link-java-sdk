package sast.sastlink.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @projectName: sast-link-SDK
 * @author: feelMoose
 * @date: 2023/7/16 21:35
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    protected String userId;
    protected String email;
    protected String avatar;
    protected Badge badge;
    protected String bio;
    protected String dep;
    protected List<String> hide;
    protected List<String> link;
    protected String nickname;
    protected String org;
}
