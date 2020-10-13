package robhopkins.wc.professors;

import io.vertx.core.Vertx;

public final class Main {
    public static void main(final String... args) throws Exception {
        // TODO: Figure out datastore and inject.

        final Main main = new Main();
        main.start();
    }

    private final ProfessorVerticle verticle;
    private Main() {
        verticle = new ProfessorVerticle();
    }

    private void start() throws Exception {
        Vertx.vertx().deployVerticle(verticle);
    }
}
