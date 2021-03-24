package robhopkins.wc.professors.db;

import org.json.JSONObject;
import robhopkins.wc.common.datasource.DatasourceRequest;
import robhopkins.wc.common.datasource.DatasourceRequestBuilder;
import robhopkins.wc.common.datasource.DatasourceResponse;
import robhopkins.wc.professors.exception.ServerException;

public final class UpdateActionSpy implements DatasourceAction {

    private JSONObject payload;
    private final DatasourceResponse[] responses;
    private int current;

    public UpdateActionSpy(final DatasourceResponse... responses) {
        this.responses = responses;
    }

    public JSONObject payload() {
        return payload;
    }

    @Override
    public DatasourceResponse execute(final DatasourceRequestBuilder builder) throws ServerException {
        final DatasourceRequest request = builder.build();
        if (builderIsUpdate(request)) {
            payload = request.payload();
        }
        return responses[current++];
    }

    private boolean builderIsUpdate(final DatasourceRequest request) {
        return "UPDATE".equalsIgnoreCase(request.type());
    }
}
