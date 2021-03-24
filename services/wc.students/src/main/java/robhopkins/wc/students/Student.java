package robhopkins.wc.students;


import robhopkins.wc.students.domain.ObjectId;

public interface Student {
    ObjectId id();
    void populate(StudentPopulator populator);

    interface StudentPopulator {
        void firstName(String value);
        void lastName(String value);
        void id(ObjectId id);
        void facultyId(ObjectId id);
        void email(String value);
    }
}
