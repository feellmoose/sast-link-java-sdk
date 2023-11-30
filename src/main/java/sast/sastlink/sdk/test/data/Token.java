package sast.sastlink.sdk.test.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import sast.sastlink.sdk.model.response.data.BaseData;

@Data
@NoArgsConstructor
public class Token implements BaseData {
    private String loginToken;
}
