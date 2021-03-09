package robhopkins.wc.db.request;

public final class DataOperation {

    public enum OperationType {
        QUERY,
        INSERT,
        UPDATE,
        DELETE,
        DDL;
    }

    private final String namespace;
    private final Object options;
    private final OperationType type;

    public DataOperation(final String namespace, final OperationType type, final Object options) {
        this.namespace = namespace;
        this.options = options;
        this.type = type;
    }

    public String getNamespace() {
        return namespace;
    }

    public Object getOptions() {
        return options;
    }

    public OperationType getType() {
        return type;
    }
}
