package fun.feellmoose.service.impl;


import fun.feellmoose.constants.LinkParamConstants;
import fun.feellmoose.service.SastLinkService;
import fun.feellmoose.exception.SastLinkException;

public sealed abstract class AbstractSastLinkService implements SastLinkService, LinkParamConstants permits HttpClientSastLinkService, OkHttpSastLinkService, RestTemplateSastLinkService {
    protected final String client_id;
    protected final String client_secret;
    protected final String code_verifier;
    protected final String redirect_uri;
    protected final String host_name;

    protected AbstractSastLinkService(Builder<? extends SastLinkService.Builder<?>> builder) {
        client_id = builder.client_id;
        client_secret = builder.client_secret;
        code_verifier = builder.code_verifier;
        redirect_uri = builder.redirect_uri;
        host_name = builder.host_name;
    }

    public abstract static class Builder<B extends Builder<B>> implements SastLinkService.Builder<B> {
        public Builder() {
        }

        protected String client_id;
        protected String client_secret;
        protected String code_verifier;
        protected String redirect_uri;
        protected String host_name;

        public B setClientId(String client_id) {
            self().client_id = client_id;
            return self();
        }

        public B setClientSecret(String client_secret) {
            self().client_secret = client_secret;
            return self();
        }

        @Deprecated
        public B setCodeVerifier(String code_verifier) {
            self().code_verifier = code_verifier;
            return self();
        }

        @Deprecated
        public B setRedirectUri(String redirect_uri) {
            self().redirect_uri = redirect_uri;
            return self();
        }

        public B setHostName(String host_name) {
            self().host_name = host_name;
            return self();
        }

        protected abstract B self();

        @Override
        public SastLinkService build() {
            if (this.client_id.isEmpty() || this.client_secret.isEmpty()) {
                throw new SastLinkException("redirect_uri, client_id or client_secret not exist");
            }
            if (this.host_name.isEmpty()) {
                throw new SastLinkException("sast-link server host_name is needed in building a sastLinkService");
            }
            return (SastLinkService) null;
        }
    }

}
