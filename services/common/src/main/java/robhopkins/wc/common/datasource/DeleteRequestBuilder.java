package robhopkins.wc.common.datasource;

import org.json.JSONObject;

public final class DeleteRequestBuilder extends DatasourceRequestBuilder {
    public static DeleteRequestBuilder newBuilder() {
        return new DeleteRequestBuilder();
    }

    private String namespace;
    private String table;
    private final Object[] where = new Object[2];

    private DeleteRequestBuilder() {

    }

    public DeleteRequestBuilder withNamespace(final String namespace) {
        this.namespace = namespace;
        return this;
    }

    public DeleteRequestBuilder forTable(final String table) {
        this.table = table;
        return this;
    }

    public DeleteRequestBuilder where(final String field, final Object value) {
        this.where[0] = field;
        this.where[1] = value;
        return this;
    }

    @Override
    JSONObject payload() {
        return new JSONObject()
            .put("namespace", namespace)
            .put("options", options())
            .put("type", "DELETE");
    }

    private Object options() {
        return new JSONObject()
            .put("table", table)
            .put("id", new JSONObject().put(where[0].toString(), where[1]));
    }
}
