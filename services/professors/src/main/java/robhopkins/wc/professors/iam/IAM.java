package robhopkins.wc.professors.iam;

import robhopkins.wc.professors.auth.AuthenticationException;

/**
 * Represents the various Identity and Access Management actions.
 */
public interface IAM {

    /**
     * Tests whether or not a token is valid.
     *
     * @param token a {@link Token} implementation to be tested.
     *
     * @return the token, if it valid.
     *
     * @throws AuthenticationException if the token is invalid.
     */
    Token testToken(Token token) throws AuthenticationException;
}
