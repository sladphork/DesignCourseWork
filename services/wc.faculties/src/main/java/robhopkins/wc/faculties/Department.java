package robhopkins.wc.faculties;

import robhopkins.wc.faculties.domain.ObjectId;

public interface Department {
    ObjectId id();
    void populate(DepartmentPopulator populator);

    interface DepartmentPopulator {
        void id(ObjectId id);
        void name(String name);
    }
}
