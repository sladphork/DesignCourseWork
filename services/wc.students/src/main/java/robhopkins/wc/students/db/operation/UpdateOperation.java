package robhopkins.wc.students.db.operation;

import org.jboss.logmanager.LogManager;
import robhopkins.wc.common.datasource.DatasourceResponse;
import robhopkins.wc.common.datasource.UpdateRequestBuilder;
import robhopkins.wc.students.Student;
import robhopkins.wc.students.db.DatasourceService;
import robhopkins.wc.students.exception.ServerException;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

final class UpdateOperation implements Operation<String> {
    private final Student student;
    private final Student original;
    private final DatasourceService service;

    UpdateOperation(final Student student, final Student original, final DatasourceService service) {
        this.service = service;
        this.student = student;
        this.original = original;
    }

    @Override
    public String execute() throws ServerException {
        final DatasourceResponse response = service.execute(
            UpdateRequestBuilder.newBuilder()
                .withNamespace("students")
                .forTable("students")
                .forId("id", student.id())
                .withValues(DMLPopulator.forUpdate(student, setTests()).toMap())
                .build());

        LogManager.getLogManager().getLogger("Datasourcestudents")
            .info("\nAdded professor with response: " + response.body());
        return response.body();
    }


    private Map<String, Predicate<Object>> setTests() {
        final Map<String, Object> original = DMLPopulator.forStudent(this.original).toMap();
        return Map.of(
            "first_name", updatePredicate("first_name", original),
            "last_name", updatePredicate("last_name", original),
            "faculty_id", updatePredicate("faculty_id", original)
        );
    }

    private Predicate<Object> updatePredicate(final String name, final Map<String, Object> map) {
        return value -> {
            final Object oldValue = map.get(name);
            return (!Objects.toString(value, "").isBlank()
                && !Objects.equals(oldValue, value));
        };
    }
}
