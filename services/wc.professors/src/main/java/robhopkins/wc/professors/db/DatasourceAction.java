package robhopkins.wc.professors.db;

import robhopkins.wc.common.datasource.DatasourceRequestBuilder;
import robhopkins.wc.common.datasource.DatasourceResponse;
import robhopkins.wc.professors.exception.ServerException;

public interface DatasourceAction {

    /**
     *
     * @param baseUri
     * @param builder
     * @return
     */
    DatasourceResponse execute(DatasourceRequestBuilder builder) throws ServerException;
}
