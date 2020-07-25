package robhopkins.wc.iam.user.domain;

public final class Name {
    public static Name from(final Object firstName, final Object lastName) {
        return new Name(firstName.toString(), lastName.toString());
    }

    private final String firstName;
    private final String lastName;
    private Name(final String firstName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }


    @Override
    public String toString() {
        return String.format("%s %s", firstName, lastName);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        final Name name = (Name) other;

        if (firstName != null ? !firstName.equals(name.firstName) : name.firstName != null) return false;
        return lastName != null ? lastName.equals(name.lastName) : name.lastName == null;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }
}
