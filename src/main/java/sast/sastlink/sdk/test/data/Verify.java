package sast.sastlink.sdk.test.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.NoArgsConstructor;
import sast.sastlink.sdk.model.response.data.BaseData;

@Data
@NoArgsConstructor
public class Verify implements BaseData {
    @JsonAlias("login_ticket")
    private String loginTicket;
}
