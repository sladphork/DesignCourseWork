package robhopkins.wc.faculties.request;

import robhopkins.wc.faculties.Faculty;
import robhopkins.wc.faculties.domain.ObjectId;
import robhopkins.wc.faculties.Faculties;
import robhopkins.wc.faculties.FacultiesFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Map;

@ApplicationScoped
public final class RequestFaculties implements Faculties {

    private final Faculties target;
    public RequestFaculties() {
        target = FacultiesFactory.newFactory().create();
    }

    @Override
    public void configure(final Map<String, Object> properties) {
        target.configure(properties);
    }

    @Override
    public Collection<Faculty> getAll() {
        return target.getAll();
    }
}
