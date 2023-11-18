package sast.sastlink.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sast.sastlink.sdk.model.response.data.BaseData;


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
