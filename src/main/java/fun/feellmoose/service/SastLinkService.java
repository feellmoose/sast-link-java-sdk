package fun.feellmoose.service;

import fun.feellmoose.model.response.data.AccessToken;
import fun.feellmoose.exception.SastLinkException;
import fun.feellmoose.model.response.data.RefreshToken;
import fun.feellmoose.model.response.data.User;

public interface SastLinkService {
    AccessToken accessToken(String code) throws SastLinkException;

    /* 刷新accessToken */
    RefreshToken refreshToken(String refresh_token) throws SastLinkException;

    /* 使用accessToken获取用户信息 */
    User user(String accessToken) throws SastLinkException;

    interface Builder<B extends Builder<B>> {
        SastLinkService build();
    }

}
