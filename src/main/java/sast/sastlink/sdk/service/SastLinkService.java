package sast.sastlink.sdk.service;

import sast.sastlink.sdk.exception.SastLinkException;
import sast.sastlink.sdk.model.UserInfo;
import sast.sastlink.sdk.model.response.AccessTokenData;
import sast.sastlink.sdk.model.response.RefreshResponse;

public interface SastLinkService<T extends SastLinkService<T>> {
    AccessTokenData accessToken(String code) throws SastLinkException;

    /* 刷新accessToken */
    RefreshResponse refresh(String refresh_token) throws SastLinkException;

    /* 使用accessToken获取用户信息 */
    UserInfo userInfo(String accessToken) throws SastLinkException;

    interface Builder<T extends SastLinkService<T>> {
        T build();
    }

}
