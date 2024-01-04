package fun.feellmoose.test.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import fun.feellmoose.model.response.data.BaseData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Verify implements BaseData {
    @JsonAlias("login_ticket")
    private String loginTicket;
}
