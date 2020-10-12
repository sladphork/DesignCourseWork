package robhopkins.wc.professors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class ProfessorVerticle extends AbstractVerticle {

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
        final Router router = Router.router(getVertx());

        router.route(HttpMethod.GET, "/professors")
            .handler(ctx ->
                ctx.response().end("Hello, World!"));

        getVertx().createHttpServer().requestHandler(router).listen(8081);
        startPromise.complete();
    }

    @Override
    public void stop(final Promise<Void> stopPromise) throws Exception {
        stopPromise.complete();
    }

    private void getAll(final RoutingContext ctx) {

    }
}
