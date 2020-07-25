package robhopkins.wc.iam.user;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import robhopkins.wc.iam.user.domain.UserId;
import robhopkins.wc.iam.user.domain.Username;
import robhopkins.wc.iam.user.exception.UserNotFoundException;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

abstract class AbstractUsers implements Users {

    private final Map<UserId, User> users = new LinkedHashMap<>();

    AbstractUsers(final User... users) {
        this.users.putAll(Arrays.stream(users)
            .collect(Collectors.toMap(
                User::id,
                Function.identity()
            )));
    }

    @Override
    public final User get(final UserId id) throws UserNotFoundException {
        return users.get(id);
    }

    @Override
    public User get(final Username username) throws UserNotFoundException {
        return users.values().stream()
            .filter(user -> user.username().equals(username))
            .findFirst()
            .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public final Collection<User> find(final Predicate<User> filter) {
        return users.values().stream()
            .filter(filter)
            .collect(Collectors.toSet());
    }

    final void load() {
        this.users.putAll(
            fromJson(
                new JSONArray(new JSONTokener(jsonInput()))
            )
        );
    }

    private Map<UserId, User> fromJson(final JSONArray users) {
        return StreamSupport.stream(users.spliterator(), true)
            .map(obj -> fromJson((JSONObject) obj))
            .collect(Collectors.toMap(
                User::id,
                Function.identity()
            ));
    }

    abstract InputStream jsonInput();

    abstract User fromJson(JSONObject json);
}
