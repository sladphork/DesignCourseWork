package robhopkins.wc.faculties;

import robhopkins.wc.faculties.domain.ObjectId;

import java.util.Collection;

public interface Faculty {

    ObjectId id();

    void populate(FacultyPopulator populator);

    interface FacultyPopulator {
        void id(ObjectId id);
        void name(String name);
        void departments(Collection<Department> departments);
    }
}
