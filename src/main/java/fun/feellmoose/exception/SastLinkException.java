package fun.feellmoose.exception;

import fun.feellmoose.enums.SastLinkErrorEnum;
import fun.feellmoose.model.response.SastLinkResponse;
import fun.feellmoose.util.JsonUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        super(message);
        this.errorEnum = SastLinkErrorEnum.COMMON_ERROR;
    }

    public SastLinkException(SastLinkResponse<?> response) {
        super(getErrorMessage(response));
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
            if (msg == null) msg = "";
            //append if msg is from sast-link
            if (msg.contains("Bad Request:")) {
                SastLinkResponse<?> sastLinkResponse = JsonUtil.fromJson(msg.substring(msg.indexOf("Bad Request:") + 14, msg.length() - 1), SastLinkResponse.class);
                errorMessageBuilder
                        .append(" Exception: Bad Request: sast-link error ErrCode: ")
                        .append(sastLinkResponse.getErrCode())
                        .append(", ErrMsg:")
                        .append(sastLinkResponse.getErrMsg());
            }
        }
        return errorMessageBuilder.toString();
    }

    private static String getErrorMessage(@NotNull SastLinkResponse<?> response) {
        if (response.isSuccess()) {
            return "logic error, message needs to be handled successfully: " + response;
        } else {
            return "Exception: sast-link error " +
                    "RESPONSE: ErrCode: " + response.getErrCode() + ", ErrMsg:" + response.getErrMsg();
        }
    }

}
