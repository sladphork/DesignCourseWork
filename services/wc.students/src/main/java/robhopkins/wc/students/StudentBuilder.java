package robhopkins.wc.students;

import robhopkins.wc.students.domain.ObjectId;

/**
 * A simple <a href="">Builder</a> that can be used to create a {@link Student}.
 */
public final class StudentBuilder implements Student {

    /**
     * Creates a new {@code StudentBuilder} instance.
     *
     * @return a {@code StudentBuilder} instance.
     */
    public static StudentBuilder newBuilder() {
        return new StudentBuilder();
    }

    /**
     * Creates a new {@code StudentBuilder} instance using the supplied {@link Student}
     * as a source.
     *
     * @param student a {@link Student} to use as a source.
     *
     * @return a {@code StudentBuilder} instance.
     */
    public static StudentBuilder newBuilder(final Student student) {
        final StudentBuilder builder = new StudentBuilder();
        student.populate(new CopyPopulator(builder));
        return builder;
    }

    private String firstName;
    private String lastName;
    private String email;
    private ObjectId id;
    private ObjectId facultyId;

    private StudentBuilder() {
    }

    public StudentBuilder withFirstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }

    public StudentBuilder withLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    public StudentBuilder withId(final ObjectId id) {
        this.id = id;
        return this;
    }

    public StudentBuilder withFacultyId(final ObjectId facultyId) {
        this.facultyId = facultyId;
        return this;
    }

    public StudentBuilder withEmail(final String email) {
        this.email = email;
        return this;
    }

    @Override
    public ObjectId id() {
        return id;
    }

    @Override
    public void populate(final StudentPopulator populator) {
        populator.facultyId(facultyId);
        populator.id(id);
        populator.firstName(firstName);
        populator.lastName(lastName);
        populator.email(email);
    }

    public Student build() {
        return this;
    }

    private static final class CopyPopulator implements StudentPopulator {

        private final StudentBuilder builder;

        private CopyPopulator(final StudentBuilder builder) {
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

        @Override
        public void email(final String value) {
            builder.withEmail(value);
        }
    }
}
