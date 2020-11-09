package robhopkins.wc.professors.domain;

import java.util.UUID;

public final class ObjectId {

    public static final ObjectId EMPTY = new ObjectId("");

    public static ObjectId from(final Object value) {
        // Add null check?
        final String  valueToUse = value.toString();
        return valueToUse.isBlank()
            ? EMPTY
            : new ObjectId(valueToUse);
    }

    public static ObjectId random() {
        return from(UUID.randomUUID());
    }

    private final String value;
    private ObjectId(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        final ObjectId userId = (ObjectId) other;

        return value != null ? value.equals(userId.value) : userId.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
