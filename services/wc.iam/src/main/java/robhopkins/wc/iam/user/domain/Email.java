package robhopkins.wc.iam.user.domain;

public final class Email {
    public static Email from(final Object value) {
        return new Email(value.toString());
    }

    private final String value;
    private Email(final String value) {
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

        final Email username = (Email) other;

        return value != null ? value.equals(username.value) : username.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
