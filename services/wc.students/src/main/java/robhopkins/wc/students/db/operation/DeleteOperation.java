package robhopkins.wc.students.db.operation;

import robhopkins.wc.common.datasource.DatasourceResponse;
import robhopkins.wc.common.datasource.DeleteRequestBuilder;
import robhopkins.wc.students.db.DatasourceService;
import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.exception.ServerException;

final class DeleteOperation implements Operation<String> {

    private final ObjectId studentId;
    private final DatasourceService service;

    DeleteOperation(final ObjectId studentId, final DatasourceService service) {
        this.service = service;
        this.studentId = studentId;
    }

    @Override
    public String execute() throws ServerException {
        final DatasourceResponse response = service.execute(
            DeleteRequestBuilder.newBuilder()
                .withNamespace("students")
                .forTable("students")
                .where("id", studentId)
                .build()
        );

        return response.body();
    }
}
