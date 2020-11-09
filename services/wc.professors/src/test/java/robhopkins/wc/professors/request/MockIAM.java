package robhopkins.wc.professors.request;

import io.quarkus.test.Mock;
import robhopkins.wc.professors.iam.IAM;
import robhopkins.wc.professors.iam.Token;
import robhopkins.wc.professors.iam.exception.ForbiddenException;
import robhopkins.wc.professors.iam.exception.UnauthenticatedException;

import java.util.Objects;

@Mock
final class MockIAM implements IAM {

    private static UnauthenticatedException unauthenticatedException;
    static void throwException() {
        MockIAM.unauthenticatedException = new UnauthenticatedException();
    }

    private static ForbiddenException forbidden;
    static void throwForbidden() {
        forbidden = new ForbiddenException();
    }

    static void clear() {
        unauthenticatedException = null;
        forbidden = null;
    }

    @Override
    public Token validate(final String role) throws UnauthenticatedException, ForbiddenException {
        if (Objects.nonNull(unauthenticatedException)) {
            throw unauthenticatedException;
        }
        if (Objects.nonNull(forbidden)) {
            throw forbidden;
        }
        return Token.EMPTY;
    }
}
