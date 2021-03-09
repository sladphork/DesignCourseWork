package robhopkins.wc.iam.request.users;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import robhopkins.wc.iam.request.exception.IAMException;
import robhopkins.wc.iam.request.exception.InvalidBodyException;
import robhopkins.wc.iam.user.User;
import robhopkins.wc.iam.user.domain.Email;
import robhopkins.wc.iam.user.domain.Name;
import robhopkins.wc.iam.user.domain.Role;
import robhopkins.wc.iam.user.domain.Username;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.fail;

final class AddUserTest {

    @ParameterizedTest
    @ValueSource(strings = {"firstName,lastName,role", "firstName", "lastName", "role"})
    void fromShouldFailWithMissingRequiredFields(final String fields) {
        try {
            AddUser.from(createBody(json -> removeFields(json, fields)));
        } catch (IAMException e) {
            assertThat(e, missingFields("AddUser", fields));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"firstName", "lastName", "role"})
    void fromShouldFailWithEmptyFields(final String field) {
        try {
            AddUser.from(createBody(json -> json.put(field, " ")));
        } catch (IAMException e) {
            assertThat(e, missingFields("AddUser", field));
        }
    }

    @Test
    void fromShouldFailIfRoleInvalid() {
        try {
            AddUser.from(createBody(json -> json.put("role", "Invalid")));
            fail("Expected exception not thrown!");
        } catch (IAMException e) {
            assertThat(400, is(e.status()));
            assertThat(e.getMessage(), containsString("Role"));
            assertThat(e.getMessage(), containsString("Invalid"));
        }
    }

    @Test
    void toUserShouldReturnUserValue() throws Exception {
        final AddUser addUser = AddUser.from(createBody());
        final User user = addUser.toUser();
        assertThat(user, notNullValue());
    }

    @Test
    void toUserShouldReturnUserWithValidValues() throws Exception {
        final AddUser addUser = AddUser.from(createBody());
        final User user = addUser.toUser();
        assertThat(user, isExpectedUser());
    }

    @ParameterizedTest
    @ValueSource(strings = {"username", "email"})
    void toUserShouldUserDefaultValuesIfBlank(final String field) throws Exception {
        final AddUser addUser = AddUser.from(
            createBody(json -> json.put(field, "  "))
        );
        final User user = addUser.toUser();
        assertThat(user, isExpectedUser());
    }

    @Test
    void toUserShouldUseUsernameIfProvided() throws Exception {
        final AddUser addUser = AddUser.from(
            createBody(json -> json.put("username", "username"))
        );
        final User user = addUser.toUser();
        assertThat(user, isExpectedUser(Username.from("username")));
    }

    @Test
    void toUserShouldUseEmailIfProvided() throws Exception {
        final AddUser addUser = AddUser.from(
            createBody(json -> json.put("email", "user.email@someplace.com"))
        );
        final User user = addUser.toUser();
        assertThat(user, isExpectedUser(Email.from("user.email@someplace.com")));
    }

    private Matcher<User> isExpectedUser() {
        return isExpectedUser(Username.from("tuser"), Email.from("Test.User@wc.edu"));
    }

    private Matcher<User> isExpectedUser(final Email email) {
        return isExpectedUser(Username.from("tuser"), email);
    }

    private Matcher<User> isExpectedUser(final Username username) {
        return isExpectedUser(username, Email.from("Test.User@wc.edu"));
    }
    private Matcher<User> isExpectedUser(final Username username, final Email email) {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(final Object o) {
                final User user = (User) o;
                return user.name().equals(Name.from("Test", "User"))
                    && user.role().equals(Role.STUDENT)
                    && user.username().equals(username)
                    && user.email().equals(email)
                    && Objects.nonNull(user.id());
            }

            @Override
            public void describeTo(final Description description) {
            }
        };
    }

    private Matcher<IAMException> missingFields(final String type, final String fields) {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(final Object o) {
                final InvalidBodyException ex = (InvalidBodyException) o;
                final String message = ex.getMessage();
                return 400 == ex.status()
                    && message.contains(type)
                    && Arrays.stream(fields.split(","))
                    .anyMatch(message::contains);
            }

            @Override
            public void describeTo(final Description description) {
            }
        };
    }

    private String createBody() {
        return createBody(Function.identity());
    }

    private String createBody(final Function<JSONObject, JSONObject> mapper) {
        return mapper.apply(
            new JSONObject()
                .put("firstName", "Test")
                .put("lastName", "User")
                .put("role", "STUDENT")
        ).toString(2);
    }

    private JSONObject removeFields(final JSONObject json, final String fields) {
        Arrays.stream(fields.split(","))
            .forEach(json::remove);
        return json;
    }
}
