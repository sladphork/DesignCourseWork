package robhopkins.wc.students.db;

import robhopkins.wc.common.datasource.DatasourceRequest;
import robhopkins.wc.common.datasource.DatasourceResponse;
import robhopkins.wc.students.exception.ServerException;

public interface DatasourceService {

    DatasourceResponse execute(DatasourceRequest request) throws ServerException;
}
