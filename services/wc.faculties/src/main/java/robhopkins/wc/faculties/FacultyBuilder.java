package robhopkins.wc.faculties;

import robhopkins.wc.faculties.domain.ObjectId;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class FacultyBuilder implements Faculty {

    public static FacultyBuilder newBuilder() {
        return new FacultyBuilder();
    }

    private ObjectId id;
    private String name;
    private final List<Department> departments;

    private FacultyBuilder() {
        this.departments = new LinkedList<>();
    }

    @Override
    public ObjectId id() {
        return id;
    }

    public FacultyBuilder withId(final ObjectId id) {
        this.id = id;
        return this;
    }

    public FacultyBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public FacultyBuilder withDepartments(final Collection<Department> departments) {
        this.departments.addAll(departments);
        return this;
    }

    public Faculty build() {
        return this;
    }

    @Override
    public void populate(final FacultyPopulator populator) {
        populator.id(id);
        populator.name(name);
        populator.departments(departments);
    }
}
