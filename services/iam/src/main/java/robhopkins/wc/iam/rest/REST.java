package robhopkins.wc.iam.rest;

import org.takes.Take;
import org.takes.facets.fork.FkMethods;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.Fork;
import org.takes.facets.fork.TkFork;
import org.takes.tk.TkCors;
import org.takes.tk.TkSlf4j;
import robhopkins.wc.iam.signin.SigninSheet;

public final class REST {

    public static REST create(final SigninSheet sheet) {
        return new REST(sheet);
    }

    private final SigninSheet sheet;

    REST(final SigninSheet sheet) {
        this.sheet = sheet;
    }

    public Take toTake() {
        return new TkSlf4j(
            new TkCors(
                new TkFork(
                    signin(),
                    signout(),
                    token()
                ),
                "http://localhost:3000",
                "http://localhost:8080",
                "http://localhost"
        )
        );
    }

    private Fork token() {
        return new FkRegex("/iam/token",
            new TkFork(
                new FkMethods("POST", new TokenRequest(sheet))
            )
        );
    }

    private Fork signout() {
        return new FkRegex("/iam/signout",
            new TkFork(
                new FkMethods("POST", new SignoutRequest(sheet))
            )
        );
    }

    private Fork signin() {
        return new FkRegex("/iam/signin",
            new TkFork(
                new FkMethods("POST", new SigninRequest(sheet))
            )
        );
    }
}
