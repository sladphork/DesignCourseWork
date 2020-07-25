package robhopkins.wc.iam.rest;

import org.junit.jupiter.api.Test;
import org.takes.Request;
import org.takes.Response;
import robhopkins.wc.iam.signin.Credentials;
import robhopkins.wc.iam.signin.SigninSheet;
import robhopkins.wc.iam.signin.Token;
import robhopkins.wc.iam.user.exception.AuthenticationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static robhopkins.wc.iam.rest.ResponseMatchers.*;

final class SignoutRequestTest {

    @Test
    void requestShouldHandleNoAuthHeader() throws Exception {
        final SigninSheetSpy spy = new SigninSheetSpy();
        final SignoutRequest handler = new SignoutRequest(spy);

        final Request request = RequestBuilder.newBuilder()
            .build();

        final Response response = handler.act(request);
        assertThat(response, notNullValue());
        assertThat(response, isBadResponse());
    }

    @Test
    void requestShouldHandleNoToken() throws Exception {
        final SigninSheetSpy spy = new SigninSheetSpy();
        final SignoutRequest handler = new SignoutRequest(spy);

        final Request request = RequestBuilder.newBuilder()
            .withHeader("Authorization", "")
            .build();

        final Response response = handler.act(request);
        assertThat(response, notNullValue());
        assertThat(response, isBadResponse());
    }

    @Test
    void requestShouldHandleTokenForUnknownUser() throws Exception {
        final SigninSheetSpy spy = new SigninSheetSpy();
        final SignoutRequest handler = new SignoutRequest(spy);

        final Request request = RequestBuilder.newBuilder()
            .withHeader("Authorization", "ewogICJzdWIiOiAidGVzdHMiLAogICJyb2xlIjogIlNUVURFTlQiLAogICJpc3MiOiAiV3lsaWUgQ29sbGVnZSIsCiAgIm5hbWUiOiAiVGVzdCBTdHVkZW50IiwKICAiZXhwIjogIjIwMjAtMDctMjVUMjI6MDk6NDcuMDAxNjE4WiIsCiAgInVzZXIiOiAiNTM1ODY3YjMtOTVkOS00ZDM4LTkxMGItMjI5YTMwMWExZDgxIgp9")
            .build();

        final Response response = handler.act(request);
        assertThat(response, notNullValue());
    }

    private static final class SigninSheetSpy extends SigninSheet {

        private boolean signoutCalled;
        @Override
        public Token signin(final Credentials credentials) throws AuthenticationException {
            return null;
        }

        @Override
        public void signout(final Token token) {
            signoutCalled = true;
        }
    }
}
