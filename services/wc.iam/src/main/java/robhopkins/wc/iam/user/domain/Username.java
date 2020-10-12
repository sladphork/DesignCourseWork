package robhopkins.wc.iam.user.domain;

public class Username {
    public static Username from(final Object value) {
        return new Username(value.toString());
    }

    private final String value;
    private Username(final String value) {
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

        final Username username = (Username) other;

        return value != null ? value.equals(username.value) : username.value == null;
    }

    public boolean equals(final String value) {
        return equals(Username.from(value));
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
