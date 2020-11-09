package robhopkins.wc.professors;

import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.exception.ProfessorNotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

final class InMemProfessors implements Professors {

    private final Map<ObjectId, Professor> data;

    InMemProfessors() {
        data = new ConcurrentHashMap<>();
    }

    @Override
    public Professor get(final ObjectId id) throws ProfessorNotFoundException {
        return Optional.ofNullable(data.get(id))
            .orElseThrow(() -> new ProfessorNotFoundException(id));
    }

    @Override
    public Professor add(final Professor professor) {
        final Professor added = ProfessorBuilder.newBuilder(professor)
            .withId(ObjectId.random())
            .build();
        data.put(added.id(), added);
        return added;
    }

    @Override
    public Professor update(final Professor professor) throws ProfessorNotFoundException {
        final UpdateOperation operation =
            new UpdateOperation(
                get(professor.id()
                )
            );
        final Professor updated = operation.update(professor);
        data.put(updated.id(), updated);
        return updated;
    }

    @Override
    public void delete(final ObjectId id) {
        // The delete will always work, even if the professor is already gone.
        data.remove(id);
    }

    @Override
    public Collection<Professor> getAll() {
        return Collections.unmodifiableCollection(data.values());
    }

    void clear() {
        data.clear();
    }

    void put(final Professor... professors) {
        data.putAll(
            Arrays.stream(professors)
                .collect(Collectors.toMap(
                    Professor::id,
                    Function.identity()
                ))
        );
    }

    private static final class UpdateOperation implements Professor.ProfessorPopulator {

        private final ProfessorBuilder builder;
        UpdateOperation(final Professor toUpdate) {
            builder = ProfessorBuilder.newBuilder(toUpdate);
        }

        Professor update(final Professor source) {
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

        private boolean doUpdate(final Object value) {
            return Objects.nonNull(value) && !value.toString().isBlank();
        }
    }
}
