package robhopkins.wc.professors;

import robhopkins.wc.common.datasource.DatasourceRequestBuilder;
import robhopkins.wc.common.datasource.DatasourceResponse;
import robhopkins.wc.professors.db.DatasourceAction;
import robhopkins.wc.professors.db.DatasourceProfessors;
import robhopkins.wc.professors.db.EventProfessors;
import robhopkins.wc.professors.exception.ServerException;

import java.io.IOException;

public final class ProfessorsFactory {
    public static ProfessorsFactory newFactory() {
        return new ProfessorsFactory();
    }

    private ProfessorsFactory() {
    }

    public Professors create(final String dsUri) {
        // We can control this based on config options, etc.
        return new EventProfessors(new DatasourceProfessors(new DatasourceRequestAction(dsUri)));
    }

    private static final class DatasourceRequestAction implements DatasourceAction {
        private final String baseUri;
        DatasourceRequestAction(final String baseUri) {
            this.baseUri = baseUri;
        }
        @Override
        public DatasourceResponse execute(final DatasourceRequestBuilder builder)
            throws ServerException {

            try {
                return builder.build().execute(this.baseUri);
            } catch (IOException e) {
                throw new ServerException(e);
            }
        }
    }
}
