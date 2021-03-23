package robhopkins.wc.common.datasource;

import org.json.JSONObject;

public abstract class DatasourceRequestBuilder {

    protected DatasourceRequestBuilder() {
    }

    public final DatasourceRequest build() {
        return new HttpDatasourceRequest(payload());
    }

    abstract JSONObject payload();
}
