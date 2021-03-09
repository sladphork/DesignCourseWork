package robhopkins.wc.iam.user;

import robhopkins.wc.iam.request.exception.AuthenticationException;

public interface Secret {

    void check(byte[] toCheck) throws AuthenticationException;

    void set(byte[] value);

    void set();
}
