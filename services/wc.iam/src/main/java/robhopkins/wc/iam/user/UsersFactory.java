package robhopkins.wc.iam.user;

import robhopkins.wc.iam.user.db.HSQLUsersFactory;

public abstract class UsersFactory {
    public static UsersFactory newFactory() {
        return new HSQLUsersFactory();
    }

    protected UsersFactory() {
    }

    public abstract Users create();
}
