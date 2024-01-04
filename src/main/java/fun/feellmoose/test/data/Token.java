package fun.feellmoose.test.data;

import fun.feellmoose.model.response.data.BaseData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Token implements BaseData {
    private String loginToken;
}
