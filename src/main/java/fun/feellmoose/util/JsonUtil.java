package fun.feellmoose.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.feellmoose.enums.SastLinkErrorEnum;
import fun.feellmoose.exception.SastLinkException;

public class JsonUtil {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String toJson(T t) {
        String json;
        try {
            json = objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new SastLinkException(SastLinkErrorEnum.ERROR_ENCODE, e);
        }
        return json;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        T instance;
        try {
            instance = objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new SastLinkException(SastLinkErrorEnum.ERROR_DECODE, e);
        }
        return instance;
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        T instance;
        try {
            instance = objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            throw new SastLinkException(SastLinkErrorEnum.ERROR_DECODE, e);
        }
        return instance;
    }

}
