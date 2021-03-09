package robhopkins.wc.iam.user.db;

import robhopkins.wc.iam.user.User;
import robhopkins.wc.iam.user.UserBuilder;
import robhopkins.wc.iam.user.domain.Role;

import javax.management.relation.RoleInfo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "iam_users")
public class HibernateUser {

    @Id
    @Column(name ="id")
    private String id;
    @Column(name ="first_name")
    private String firstName;
    @Column(name ="last_name")
    private String lastName;
    @Column(name ="email")
    private String email;
    @Column(name ="username")
    private String username;
    @Column(name ="role")
    private String role;

    public HibernateUser() {

    }

    HibernateUser(final User user) {
        setId(user.id().toString());
        setFirstName(user.name().firstName());
        setLastName(user.name().lastName());
        setEmail(user.email().toString());
        setRole(user.role().toString());
        setUsername(user.username().toString());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    User toUser() {
        return UserBuilder.newBuilder()
            .withId(getId())
            .withFirstName(getFirstName())
            .withLastName(getLastName())
            .withEmail(getEmail())
            .withUsername(getUsername())
            .withRole(Role.valueOf(getRole().toUpperCase()))
            .build();
    }
}
