package robhopkins.wc.iam.sheet;

public interface Credentials {

    String username();

    byte[] secret();
}
