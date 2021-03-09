package robhopkins.wc.faculties.request;

import org.json.JSONArray;
import org.json.JSONObject;
import robhopkins.wc.faculties.Department;
import robhopkins.wc.faculties.Faculty;
import robhopkins.wc.faculties.domain.ObjectId;

import java.util.Collection;

final class JsonPopulator implements Faculty.FacultyPopulator {

    private final JSONObject json;
    private final JSONArray departments;
    private final Faculty faculty;

    JsonPopulator(final Faculty faculty) {
        json = new JSONObject();
        departments = new JSONArray();
        json.put("departments", departments);
        this.faculty = faculty;
    }

    @Override
    public void id(final ObjectId id) {
        json.put("id", id.toString());
    }

    @Override
    public void name(final String name) {
        json.put("name", name);
    }

    @Override
    public void departments(final Collection<Department> departments) {
        departments.forEach(department ->
            this.departments.put(mapDepartment(department))
        );
    }

    private JSONObject mapDepartment(final Department department) {
        final JsonDepartmentPopulator populator = new JsonDepartmentPopulator();
        department.populate(populator);
        return populator.department;
    }

    @Override
    public String toString() {
        faculty.populate(this);
        return json.toString(2);
    }

    private static final class JsonDepartmentPopulator implements Department.DepartmentPopulator {

        final JSONObject department;
        JsonDepartmentPopulator() {
            this.department = new JSONObject();
        }

        @Override
        public void id(final ObjectId id) {
            department.put("id", id.toString());
        }

        @Override
        public void name(final String name) {
            department.put("name", name);
        }
    }
}
