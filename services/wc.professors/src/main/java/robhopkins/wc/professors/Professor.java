package robhopkins.wc.professors;

import robhopkins.wc.professors.domain.ObjectId;

public interface Professor {
    ObjectId id();
    void populate(ProfessorPopulator populator);

    interface ProfessorPopulator {
        void firstName(String value);
        void lastName(String value);
        void id(ObjectId id);
        void departmentId(ObjectId id);
        void email(String value);
    }
}
