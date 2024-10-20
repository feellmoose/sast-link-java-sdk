package fun.feellmoose.service;

import fun.feellmoose.exception.SastLinkException;
import fun.feellmoose.model.response.data.AccessToken;
import fun.feellmoose.model.response.data.RefreshToken;
import fun.feellmoose.model.response.data.User;

public interface SastLinkService {

    /**
     * Get AccessToken by AuthCode
     *
     * @param code AuthCode
     * @return AccessToken
     */
    @Deprecated
    AccessToken accessToken(String code) throws SastLinkException;

    /**
     * Get AccessToken by AuthCode and verifier(optional)
     *
     * @param code         AuthCode
     * @param redirectURI  RedirectURI
     * @param codeVerifier Optional PKCE
     * @return AccessToken
     */
    AccessToken accessToken(String code, String redirectURI, String codeVerifier) throws SastLinkException;

    /**
     * Refresh token
     *
     * @param refreshToken RefreshToken
     * @return AccessToken which has been refreshed
     */
    RefreshToken refreshToken(String refreshToken) throws SastLinkException;

    /**
     * Get user info from link
     *
     * @param accessToken AccessToken
     * @return Userinfo
     */
    User user(String accessToken) throws SastLinkException;

    interface Builder<B extends Builder<B>> {
        SastLinkService build();
    }

}
