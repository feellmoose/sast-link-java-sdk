package fun.feellmoose.model.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import fun.feellmoose.model.response.data.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SastLinkResponse<T extends BaseData> {
    @JsonAlias("Data")
    private T data;
    @JsonAlias("ErrCode")
    private Integer errCode;
    @JsonAlias("ErrMsg")
    private String errMsg;
    @JsonAlias("Success")
    private boolean success;
}
