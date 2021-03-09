package robhopkins.wc.faculties;

import robhopkins.wc.faculties.domain.ObjectId;

public final class DepartmentBuilder implements Department {

    public static DepartmentBuilder newBuilder() {
        return new DepartmentBuilder();
    }

    private ObjectId id;
    private String name;

    private DepartmentBuilder() {
    }

    @Override
    public ObjectId id() {
        return id;
    }

    public DepartmentBuilder withId(final ObjectId id) {
        this.id = id;
        return this;
    }

    public DepartmentBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public Department build() {
        return this;
    }

    @Override
    public void populate(final DepartmentPopulator populator) {
        populator.id(id);
        populator.name(name);
    }
}
