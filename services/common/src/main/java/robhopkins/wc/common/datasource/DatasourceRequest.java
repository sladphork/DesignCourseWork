package robhopkins.wc.common.datasource;

import org.json.JSONObject;

import java.io.IOException;

public interface DatasourceRequest {

    DatasourceResponse execute(String baseUri) throws IOException;

    JSONObject payload();

    String type();
}
