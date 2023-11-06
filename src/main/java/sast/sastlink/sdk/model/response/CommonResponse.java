package sast.sastlink.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Map;

/**
 * @projectName: sast-link-SDK
 * @author: feelMoose
 * @date: 2023/8/19 21:42
 */
public class CommonResponse {
    @JsonAlias("Data")
    private Map<String, Object> data;
    @JsonAlias("ErrCode")
    private Integer errCode;
    @JsonAlias("ErrMsg")
    private String errMsg;
    @JsonAlias("Success")
    private boolean success;

    public CommonResponse() {
    }

    public CommonResponse(Map<String, Object> data, Integer errCode, String errMsg, boolean success) {
        this.data = data;
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.success = success;
    }

    /* Getter and Setter */

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "data=" + data +
                ", errCode=" + errCode +
                ", errMsg='" + errMsg + '\'' +
                ", success=" + success +
                '}';
    }
}




