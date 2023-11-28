package sast.sastlink.sdk.service;


public abstract class AbstractSastLinkService<T extends AbstractSastLinkService<T>> implements SastLinkService<T> {
    protected static final String authorize_response_type = "code";
    protected final String client_id;
    protected final String client_secret;
    protected final String code_verifier;
    protected final String redirect_uri;
    protected final String host_name;

    protected AbstractSastLinkService(Builder<T> builder) {
        client_id = builder.client_id;
        client_secret = builder.client_secret;
        code_verifier = builder.code_verifier;
        redirect_uri = builder.redirect_uri;
        host_name = builder.host_name;
    }

    public abstract static class Builder<T extends AbstractSastLinkService<T>> implements SastLinkService.Builder<T> {
        protected Builder() {
        }

        protected String client_id;
        protected String client_secret;
        protected String code_verifier;
        protected String redirect_uri;
        protected String host_name;

        public Builder<T> setClientId(String client_id) {
            self().client_id = client_id;
            return self();
        }

        public Builder<T> setClientSecret(String client_secret) {
            self().client_secret = client_secret;
            return self();
        }

        public Builder<T> setCodeVerifier(String code_verifier) {
            self().code_verifier = code_verifier;
            return self();
        }

        public Builder<T> setRedirectUri(String redirect_uri) {
            self().redirect_uri = redirect_uri;
            return self();
        }

        public Builder<T> setHostName(String host_name) {
            self().host_name = host_name;
            return self();
        }

        protected abstract Builder<T> self();

    }

}