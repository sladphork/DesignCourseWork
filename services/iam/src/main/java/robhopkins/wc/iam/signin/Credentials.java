package robhopkins.wc.iam.signin;

public interface Credentials {

    String username();

    byte[] secret();
}
