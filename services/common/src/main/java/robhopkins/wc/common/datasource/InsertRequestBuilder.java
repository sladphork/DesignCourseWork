package robhopkins.wc.common.datasource;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public final class InsertRequestBuilder extends DatasourceRequestBuilder {
    public static InsertRequestBuilder newBuilder() {
        return new InsertRequestBuilder();
    }

    private String namespace;
    private String table;
    private final Map<String, Object> values;

    private InsertRequestBuilder() {
        values = new LinkedHashMap<>();
    }

    public InsertRequestBuilder withNamespace(final String namespace) {
        this.namespace = namespace;
        return this;
    }

    public InsertRequestBuilder forTable(final String table) {
        this.table = table;
        return this;
    }

    public InsertRequestBuilder withValues(final Map<String, Object> values) {
        this.values.clear();
        this.values.putAll(values);
        return this;
    }

    @Override
    JSONObject payload() {
        return new JSONObject()
            .put("namespace", namespace)
            .put("options", options())
            .put("type", "INSERT");
    }

    private JSONObject options() {
        return new JSONObject()
            .put("table", table)
            .put("values", values);
    }
}
