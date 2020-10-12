package robhopkins.wc.iam.user;

import robhopkins.wc.iam.user.domain.UserId;
import robhopkins.wc.iam.user.domain.Username;
import robhopkins.wc.iam.user.exception.UserNotFoundException;

import java.util.Collection;
import java.util.function.Predicate;

public interface Users {

    User get(UserId id) throws UserNotFoundException;

    User get(Username username) throws UserNotFoundException;

    Collection<User> find(Predicate<User> filter);
}
