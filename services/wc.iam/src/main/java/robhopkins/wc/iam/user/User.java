package robhopkins.wc.iam.user;

import robhopkins.wc.iam.request.exception.AuthenticationException;
import robhopkins.wc.iam.user.domain.*;

public interface User {

    UserId id();

    Username username();

    Name name();

    Email email();

    Role role();

    void authenticate(byte[] secret) throws AuthenticationException;
}
