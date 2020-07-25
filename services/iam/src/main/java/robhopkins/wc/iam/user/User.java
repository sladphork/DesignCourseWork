package robhopkins.wc.iam.user;

import robhopkins.wc.iam.user.domain.*;
import robhopkins.wc.iam.user.exception.AuthenticationException;

public interface User {

    UserId id();

    Username username();

    Name name();

    Email email();

    Role role();

    void authenticate(byte[] secret) throws AuthenticationException;
}
