package robhopkins.wc.professors;

import robhopkins.wc.professors.domain.ObjectId;

/**
 * A simple <a href="">Builder</a> that can be used to create a {@link Professor}.
 */
public final class ProfessorBuilder implements Professor {

    /**
     * Creates a new {@code ProfessorBuilder} instance.
     *
     * @return a {@code ProfessorBuilder} instance.
     */
    public static ProfessorBuilder newBuilder() {
        return new ProfessorBuilder();
    }

    /**
     * Creates a new {@code ProfessorBuilder} instance using the supplied {@link Professor}
     * as a source.
     *
     * @param professor a {@link Professor} to use as a source.
     *
     * @return a {@code ProfessorBuilder} instance.
     */
    public static ProfessorBuilder newBuilder(final Professor professor) {
        final ProfessorBuilder builder = new ProfessorBuilder();
        professor.populate(new CopyPopulator(builder));
        return builder;
    }

    private String firstName;
    private String lastName;
    private ObjectId id;
    private ObjectId facultyId;

    private ProfessorBuilder() {
    }

    public ProfessorBuilder withFirstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ProfessorBuilder withLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    public ProfessorBuilder withId(final ObjectId id) {
        this.id = id;
        return this;
    }

    public ProfessorBuilder withFacultyId(final ObjectId facultyId) {
        this.facultyId = facultyId;
        return this;
    }

    @Override
    public ObjectId id() {
        return id;
    }

    @Override
    public void populate(final ProfessorPopulator populator) {
        populator.facultyId(facultyId);
        populator.id(id);
        populator.firstName(firstName);
        populator.lastName(lastName);
    }

    public Professor build() {
        return this;
    }

    private static final class CopyPopulator implements ProfessorPopulator {

        private final ProfessorBuilder builder;

        private CopyPopulator(final ProfessorBuilder builder) {
            this.builder = builder;
        }

        @Override
        public void firstName(final String value) {
            builder.withFirstName(value);
        }

        @Override
        public void lastName(final String value) {
            builder.withLastName(value);
        }

        @Override
        public void id(final ObjectId id) {
            builder.withId(id);
        }

        @Override
        public void facultyId(final ObjectId id) {
            builder.withFacultyId(id);
        }
    }
}
