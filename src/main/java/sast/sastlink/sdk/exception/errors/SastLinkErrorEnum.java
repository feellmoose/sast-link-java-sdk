package sast.sastlink.sdk.exception.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SastLinkErrorEnum {
    COMMON_ERROR("common error"),
    NULL_RESPONSE_BODY("response body is null"),
    EMPTY_RESPONSE_BODY("response body is empty"),
    ERROR_DECODE("error decode data from sast-link"),
    IO_ERROR("I/O error")
    ;
    final private String message;
}
