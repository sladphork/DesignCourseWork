package robhopkins.wc.db.request;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import robhopkins.wc.db.data.DataSet;
import robhopkins.wc.db.data.DataSourceException;
import robhopkins.wc.db.data.DataSourceFactory;

import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.*;

@Path("/data")
public final class DatabaseEndpoints {

    @GET
    public Response ping() {
        return Response.ok("WC Database is running!").build();
    }

    /**
     * TODO: Add check for the token to ensure the user can do this.
     *   Also, check namespace as part of this.
     * @param operation
     * @return
     */
    @POST
    public Response query(final DataOperation operation) {
        try {
            DataSet dataSet = DataSourceFactory.newFactory()
                .create(operation)
                .execute();
            return Response.ok(dataSet.toJson())
                .header("Content-type", "application/json")
                .build();
        } catch (DataSourceException e) {
            // TODO: Extend exception to supply either a sql, dml issue or something with the server.
            return Response.status(400)
                .header("Content-type", "application/vnd.wc.error.v1+json")
                .entity(e.getMessage())
                .build();
        }
    }
}
