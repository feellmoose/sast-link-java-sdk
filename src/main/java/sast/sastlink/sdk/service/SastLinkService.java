package sast.sastlink.sdk.service;

import sast.sastlink.sdk.exception.SastLinkException;
import sast.sastlink.sdk.model.response.data.AccessToken;
import sast.sastlink.sdk.model.response.data.RefreshToken;
import sast.sastlink.sdk.model.response.data.User;

public interface SastLinkService {
    AccessToken accessToken(String code) throws SastLinkException;

    /* 刷新accessToken */
    RefreshToken refreshToken(String refresh_token) throws SastLinkException;

    /* 使用accessToken获取用户信息 */
    User user(String accessToken) throws SastLinkException;

    interface Builder<T extends SastLinkService,B extends Builder<T,B>> {
        T build();
    }

}
