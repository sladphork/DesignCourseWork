package robhopkins.wc.students.db;

import org.json.JSONObject;
import robhopkins.wc.common.datasource.DatasourceRequest;
import robhopkins.wc.common.datasource.DatasourceResponse;
import robhopkins.wc.students.exception.ServerException;

import java.util.Optional;
import java.util.function.Predicate;

final class DatasourceServiceMock implements DatasourceService {

    private robhopkins.wc.common.datasource.DatasourceRequest request;
    private final Predicate<robhopkins.wc.common.datasource.DatasourceRequest> requestTest;
    private final DatasourceResponse[] responses;
    private int current;

    DatasourceServiceMock(final DatasourceResponse... responses) {
        this(request -> true, responses);
    }

    DatasourceServiceMock(final Predicate<robhopkins.wc.common.datasource.DatasourceRequest> requestTest, final DatasourceResponse... responses) {
        this.responses = responses.length > 0 ? responses : new DatasourceResponse[]{EMPTY_RESPONSE} ;
        this.requestTest = requestTest;
    }

    @Override
    public DatasourceResponse execute(final robhopkins.wc.common.datasource.DatasourceRequest request)
        throws ServerException {
        if (requestTest.test(request)) {
            this.request = request;
        }
        return responses[current++];
    }

    JSONObject payload() {
        return Optional.ofNullable(request)
            .map(DatasourceRequest::payload)
            .orElse(new JSONObject());
    }

    private static final DatasourceResponse EMPTY_RESPONSE = new DatasourceResponse() {
        @Override
        public int status() {
            return 0;
        }

        @Override
        public String body() {
            return "";
        }
    };
}
