package robhopkins.wc.faculties;

import java.util.Collection;
import java.util.Map;

public interface Faculties {

    Collection<Faculty> getAll();

    void configure(Map<String, Object> properties);
}
