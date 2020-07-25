package robhopkins.wc.iam;

import org.takes.http.Exit;
import org.takes.http.FtBasic;
import robhopkins.wc.iam.rest.REST;
import robhopkins.wc.iam.signin.SigninSheet;
import robhopkins.wc.iam.user.User;
import robhopkins.wc.iam.user.UsersFactory;
import robhopkins.wc.iam.user.domain.UserId;
import robhopkins.wc.iam.user.domain.Username;

import java.util.Collection;

public final class Main {

    public static void main(final String... args) throws Exception {

//        final User user  = UsersFactory.newFactory().create().get(UserId.from("88db9898-b856-4e38-adfa-829de5a1c3dd"));
//        final Collection<User> found = UsersFactory.newFactory().create().find(u -> Username.from("sbateup0").equals(u.username()));

        new FtBasic(
            REST.create(SigninSheet.newSheet()).toTake(),
            8080
        ).start(Exit.NEVER);
    }
}
