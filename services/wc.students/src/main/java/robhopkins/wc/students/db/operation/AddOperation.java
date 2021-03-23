package robhopkins.wc.students.db.operation;

import org.jboss.logmanager.LogManager;
import robhopkins.wc.common.datasource.DatasourceResponse;
import robhopkins.wc.common.datasource.InsertRequestBuilder;
import robhopkins.wc.students.Student;
import robhopkins.wc.students.db.DatasourceService;
import robhopkins.wc.students.exception.ServerException;

final class AddOperation implements Operation<String> {

    private final Student student;
    private final DatasourceService service;

    AddOperation(final Student student, final DatasourceService service) {
        this.service = service;
        this.student = student;
    }

    @Override
    public String execute() throws ServerException {
        final DatasourceResponse res = service.execute(
            InsertRequestBuilder.newBuilder()
                .withNamespace("students")
                .forTable("students")
                .withValues(DMLPopulator.forAdd(student).toMap())
                .build());

        LogManager.getLogManager().getLogger("DatasourceStudents")
            .info("\nAdded professor with response: " + res.body());
        return res.body();
    }
}
