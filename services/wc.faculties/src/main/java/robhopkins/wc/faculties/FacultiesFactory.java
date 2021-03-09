package robhopkins.wc.faculties;

import robhopkins.wc.faculties.db.DatasourceFaculties;

public final class FacultiesFactory {
    public static FacultiesFactory newFactory() {
        return new FacultiesFactory();
    }

    private FacultiesFactory() {
    }

    public Faculties create() {
        return new DatasourceFaculties();
    }
}
