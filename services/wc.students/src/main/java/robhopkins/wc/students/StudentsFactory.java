package robhopkins.wc.students;

import robhopkins.wc.common.datasource.DatasourceRequest;
import robhopkins.wc.common.datasource.DatasourceResponse;
import robhopkins.wc.students.db.DatasourceService;
import robhopkins.wc.students.db.DatasourceStudents;
import robhopkins.wc.students.event.EventStudents;
import robhopkins.wc.students.exception.ServerException;

import java.io.IOException;

public final class StudentsFactory {
    public static StudentsFactory newFactory(final String dsUri) {
        return new StudentsFactory(dsUri);
    }

    private final String dsUri;
    private StudentsFactory(final String dsUri) {
        this.dsUri = dsUri;
    }

    public Students create() {
        return new EventStudents(new DatasourceStudents(new DatasourceServiceRequest(dsUri)));
    }

    private static final class DatasourceServiceRequest implements DatasourceService {
        private final String baseUri;
        DatasourceServiceRequest(final String baseUri) {
            this.baseUri = baseUri;
        }
        @Override
        public DatasourceResponse execute(final DatasourceRequest request)
            throws ServerException {

            try {
                return request.execute(baseUri);
            } catch (IOException e) {
                throw new ServerException(e);
            }
        }
    }
}
