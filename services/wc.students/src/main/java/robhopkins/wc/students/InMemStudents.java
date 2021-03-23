package robhopkins.wc.students;


import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.exception.ServerException;
import robhopkins.wc.students.exception.StudentNotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

final class InMemStudents implements Students {

    private final Map<ObjectId, Student> data;

    InMemStudents() {
        data = new ConcurrentHashMap<>();
    }

    @Override
    public Student get(final ObjectId id) throws StudentNotFoundException, ServerException {
        return Optional.ofNullable(data.get(id))
            .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public Student add(final Student student) throws ServerException {
        final Student added = StudentBuilder.newBuilder(student)
            .withId(ObjectId.random())
            .build();
        data.put(added.id(), added);
        return added;
    }

    @Override
    public Student update(final Student student) throws StudentNotFoundException, ServerException {
        final UpdateOperation operation =
            new UpdateOperation(
                get(student.id()
            )
        );
        final Student updated = operation.update(student);
        data.put(updated.id(), updated);
        return updated;
    }

    @Override
    public void delete(final ObjectId id) throws ServerException {
        // The delete will always work, even if the professor is already gone.
        data.remove(id);
    }

    @Override
    public Collection<Student> getAll() throws ServerException {
        return Collections.unmodifiableCollection(data.values());
    }

    void clear() {
        data.clear();
    }

    void put(final Student... students) {
        data.putAll(
            Arrays.stream(students)
                .collect(Collectors.toMap(
                    Student::id,
                    Function.identity()
                ))
        );
    }

    private static final class UpdateOperation implements Student.StudentPopulator {

        private final StudentBuilder builder;
        UpdateOperation(final Student toUpdate) {
            builder = StudentBuilder.newBuilder(toUpdate);
        }

        Student update(final Student source) {
            source.populate(this);
            return builder.build();
        }

        @Override
        public void firstName(final String value) {
            if (doUpdate(value)) {
                builder.withFirstName(value);
            }
        }

        @Override
        public void lastName(final String value) {
            if (doUpdate(value)) {
                builder.withLastName(value);
            }
        }

        @Override
        public void id(final ObjectId id) {
            // We can't update the id.
        }

        @Override
        public void facultyId(final ObjectId id) {
            if (doUpdate(id)) {
                builder.withFacultyId(id);
            }
        }

        @Override
        public void email(String value) {
            if (doUpdate(value)) {
                builder.withEmail(value);
            }
        }

        private boolean doUpdate(final Object value) {
            return Objects.nonNull(value) && !value.toString().isBlank();
        }
    }
}
