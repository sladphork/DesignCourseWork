package robhopkins.wc.iam.user;

import robhopkins.wc.iam.request.exception.AuthenticationException;
import robhopkins.wc.iam.user.domain.*;

public interface User {
    // TODO: Change this to use a populator instead of exposing the props.

    UserId id();

    Username username();

    Name name();

    Email email();

    Role role();

    void authenticate(byte[] secret) throws AuthenticationException;
}
