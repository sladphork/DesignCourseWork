package robhopkins.wc.iam.user.domain;

import java.util.UUID;

public final class UserId {

    public static UserId from(final Object value) {
        return new UserId(value.toString());
    }

    public static UserId random() {
        return from(UUID.randomUUID());
    }

    private final String value;
    private UserId(final String value) {
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

        final UserId userId = (UserId) other;

        return value != null ? value.equals(userId.value) : userId.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
