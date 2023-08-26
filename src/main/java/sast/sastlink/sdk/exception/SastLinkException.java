package sast.sastlink.sdk.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.Nullable;
import sast.sastlink.sdk.model.response.CommonResponse;

/**
 * @projectName: sast-link-SDK
 * @author: feelMoose
 * @date: 2023/8/20 0:53
 */
public class SastLinkException extends RuntimeException {
    public SastLinkException(String message) {
        super(getErrMsg(message));
    }

    private static String getErrMsg(@Nullable String msg) {
        if (msg == null) {
            return "";
        }
        if (msg.contains("Bad Request:")) {
            try {
                CommonResponse commonResponse = new ObjectMapper().readValue(msg.substring(msg.indexOf("Bad Request:") + 14, msg.length() - 1), CommonResponse.class);
                return "ErrCode:" + commonResponse.getErrCode() + ", ErrMsg:" + commonResponse.getErrMsg();
            } catch (JsonProcessingException e) {
                return msg;
            }
        }
        return msg;
    }
}
