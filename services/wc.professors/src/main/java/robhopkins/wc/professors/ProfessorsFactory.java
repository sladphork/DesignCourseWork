package robhopkins.wc.professors;

public final class ProfessorsFactory {
    public static ProfessorsFactory newFactory() {
        return new ProfessorsFactory();
    }

    private ProfessorsFactory() {

    }

    public Professors create() {
        // We can control this based on config options, etc.
        return new InMemProfessors();
    }
}
