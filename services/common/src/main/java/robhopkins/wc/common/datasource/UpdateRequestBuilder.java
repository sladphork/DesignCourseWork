package robhopkins.wc.common.datasource;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public final class UpdateRequestBuilder extends DatasourceRequestBuilder {

    public static UpdateRequestBuilder newBuilder() {
        return new UpdateRequestBuilder();
    }

    private String namespace;
    private String table;
    private final Object[] id = new Object[2];
    private final Map<String, Object> values;

    private UpdateRequestBuilder() {
        this.values = new LinkedHashMap<>();
    }

    public UpdateRequestBuilder withNamespace(final String namespace) {
        this.namespace = namespace;
        return this;
    }

    public UpdateRequestBuilder forTable(final String table) {
        this.table = table;
        return this;
    }

    public UpdateRequestBuilder forId(final String field, final Object value) {
        this.id[0] = field;
        this.id[1] = value;
        return this;
    }

    public UpdateRequestBuilder withValues(final Map<String, Object> values) {
        this.values.clear();
        this.values.putAll(values);
        return this;
    }

    @Override
    JSONObject payload() {
        return  new JSONObject()
            .put("namespace", namespace)
            .put("options", options())
            .put("type", "UPDATE");
    }

    private Object options() {
        return new JSONObject()
            .put("table", table)
            .put("id", new JSONObject()
                .put(this.id[0].toString(), this.id[1])
            ).put("values", this.values);
    }
}
