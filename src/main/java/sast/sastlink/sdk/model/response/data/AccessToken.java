package sast.sastlink.sdk.model.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken implements BaseData {
    private String access_token;
    private Integer expires_in;
    private String refresh_token;
    private String scope;
    private String token_type;
}
