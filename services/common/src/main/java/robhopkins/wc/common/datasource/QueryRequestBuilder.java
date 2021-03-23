package robhopkins.wc.common.datasource;

import org.json.JSONObject;

import java.util.Objects;

public final class QueryRequestBuilder extends DatasourceRequestBuilder {

    public static QueryRequestBuilder newBuilder() {
        return new QueryRequestBuilder();
    }

    private String namespace;
    private String table;
    private Object[] where = new Object[2];

    private QueryRequestBuilder() {
    }

    public QueryRequestBuilder withNamespace(final String namespace) {
        this.namespace = namespace;
        return this;
    }

    public QueryRequestBuilder forTable(final String table) {
        this.table = table;
        return this;
    }

    public QueryRequestBuilder where(final String field, final Object value) {
        this.where[0] = field;
        this.where[1] = value;
        return this;
    }

    @Override
    JSONObject payload() {
        return new JSONObject()
            .put("namespace", namespace)
            .put("options", queryOptions())
            .put("type", "QUERY");
    }

    private JSONObject queryOptions() {
        final JSONObject options = new JSONObject()
            .put("table", table);
        if (Objects.nonNull(this.where[0])) {
            final JSONObject where = new JSONObject()
                .put("field", this.where[0])
                .put("value", this.where[1].toString());
            options.put("where", where);
        }
        return options;
    }
}
