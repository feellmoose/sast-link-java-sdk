package sast.sastlink.sdk.service;

import sast.sastlink.sdk.exception.SastLinkException;
import sast.sastlink.sdk.model.UserInfo;
import sast.sastlink.sdk.model.response.AccessTokenData;
import sast.sastlink.sdk.model.response.RefreshResponse;

public interface SastLinkService {
    AccessTokenData accessToken(String code) throws SastLinkException;

    /* 获取授权code */
    String authorize(String token, String code_challenge, String code_challenge_method) throws SastLinkException;

    /* 刷新accessToken */
    RefreshResponse refresh(String refresh_token) throws SastLinkException;

    /* 使用accessToken获取用户信息 */
    UserInfo userInfo(String accessToken) throws SastLinkException;

    /* 验证账号登录有效性并登录 */
    String login(String email, String password) throws SastLinkException;

    /* 登出 */
    boolean logout(String token) throws SastLinkException;

    interface Builder {
        SastLinkService build();
    }

}
