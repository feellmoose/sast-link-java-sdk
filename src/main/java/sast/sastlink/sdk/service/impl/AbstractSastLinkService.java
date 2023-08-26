package sast.sastlink.sdk.service.impl;

import sast.sastlink.sdk.service.SastLinkService;

public abstract class AbstractSastLinkService implements SastLinkService {
    protected final String client_id;
    protected final String client_secret;
    protected final String code_verifier;
    protected final String redirect_uri;
    protected final String host_name;

    protected AbstractSastLinkService(Builder<?> builder) {
        client_id = builder.client_id;
        client_secret = builder.client_secret;
        code_verifier = builder.code_verifier;
        redirect_uri = builder.redirect_uri;
        host_name = builder.host_name;
    }

    public abstract static class Builder<T extends Builder<T>> implements SastLinkService.Builder {
        protected Builder() {
        }

        protected String client_id;
        protected String client_secret;
        protected String code_verifier;
        protected String redirect_uri;
        protected String host_name;

        public T setClientId(String client_id) {
            self().client_id = client_id;
            return self();
        }

        public T setClientSecret(String client_secret) {
            self().client_secret = client_secret;
            return self();
        }

        public T setCodeVerifier(String code_verifier) {
            self().code_verifier = code_verifier;
            return self();
        }

        public T setRedirectUri(String redirect_uri) {
            self().redirect_uri = redirect_uri;
            return self();
        }

        public T setHostName(String host_name) {
            self().host_name = host_name;
            return self();
        }

        protected abstract T self();

    }

}
