package robhopkins.wc.professors.auth;

public class AuthenticationException extends Exception {

    public AuthenticationException() {
        super("User is not authenticated!");
    }
}
