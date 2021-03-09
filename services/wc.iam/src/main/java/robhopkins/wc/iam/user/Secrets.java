package robhopkins.wc.iam.user;

import robhopkins.wc.iam.user.db.HSQLSecrets;

public abstract class Secrets {
    public static Secrets newInstance() {
        return new HSQLSecrets();
    }

    public abstract Secret getFor(User user);
}
