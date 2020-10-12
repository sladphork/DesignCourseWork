package robhopkins.wc.iam.request.signin;

import robhopkins.wc.iam.sheet.Credentials;

import javax.ws.rs.FormParam;
import java.nio.ByteBuffer;

public final class SigninForm {

    @FormParam("username")
    public String username;

    @FormParam("secret")
    public String secret;

    Credentials toCredentials() {
        return new FormCredentials(
            username,
            ByteBuffer.wrap(secret.getBytes())
        );
    }

    private static final class FormCredentials implements Credentials {

        private final String username;
        private final ByteBuffer secret;
        FormCredentials(final String username, final ByteBuffer secret) {
            this.username = username;
            this.secret = secret;
            secret.clear();
        }

        @Override
        public String username() {
            return username;
        }

        @Override
        public byte[] secret() {
            return secret.array();
        }
    }
}
