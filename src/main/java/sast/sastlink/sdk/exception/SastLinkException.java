package sast.sastlink.sdk.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.lang.Nullable;
import sast.sastlink.sdk.exception.errors.SastLinkErrorEnum;
import sast.sastlink.sdk.model.response.CommonResponse;

import java.util.Optional;

/**
 * @projectName: sast-link-SDK
 * @author: feelMoose
 * @date: 2023/8/20 0:53
 */
@Getter
public class SastLinkException extends RuntimeException {
    private final SastLinkErrorEnum errorEnum;

    public SastLinkException(String message) {
        super(getErrMsg(message));
        this.errorEnum = SastLinkErrorEnum.COMMON_ERROR;
    }

    public SastLinkException(@Nullable SastLinkErrorEnum errorEnum) {
        super(getErrorMessage(errorEnum, null));
        this.errorEnum = Optional.ofNullable(errorEnum).orElse(SastLinkErrorEnum.COMMON_ERROR);
    }


    public SastLinkException(Throwable throwable) {
        super(getErrorMessage(SastLinkErrorEnum.COMMON_ERROR, throwable));
        this.errorEnum = SastLinkErrorEnum.COMMON_ERROR;
    }

    public SastLinkException(@Nullable SastLinkErrorEnum errorEnum, @Nullable Throwable throwable) {
        super(getErrorMessage(errorEnum, throwable));
        this.errorEnum = Optional.ofNullable(errorEnum).orElse(SastLinkErrorEnum.COMMON_ERROR);
    }

    private static String getErrorMessage(@Nullable SastLinkErrorEnum errorEnum, @Nullable Throwable throwable) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        if (errorEnum == null) {
            errorEnum = SastLinkErrorEnum.COMMON_ERROR;
        }
        errorMessageBuilder
                .append("sast-link-sdk error message: ")
                .append(errorEnum.getMessage());
        if (throwable != null) {
            String msg = throwable.getMessage();
            if (msg.contains(" Exception: Bad Request:")) {
                try {
                    CommonResponse commonResponse = new ObjectMapper().readValue(msg.substring(msg.indexOf("Bad Request:") + 14, msg.length() - 1), CommonResponse.class);
                    errorMessageBuilder
                            .append(" Bad Request: sast-link error ErrCode: ")
                            .append(commonResponse.getErrCode())
                            .append(", ErrMsg:")
                            .append(commonResponse.getErrMsg());
                } catch (JsonProcessingException e) {
                    errorMessageBuilder.append(msg);
                }
            }
        }
        return errorMessageBuilder.toString();
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
