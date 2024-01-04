package fun.feellmoose.model.response.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken implements BaseData {
    @JsonAlias("access_token")
    private String accessToken;
    @JsonAlias("expires_in")
    private Integer expiresIn;
    @JsonAlias("refresh_token")
    private String refreshToken;
    private String scope;
    @JsonAlias("token_type")
    private String tokenType;
}
