package robhopkins.wc.iam.user;

import robhopkins.wc.iam.request.exception.IAMException;
import robhopkins.wc.iam.user.domain.UserId;
import robhopkins.wc.iam.user.domain.Username;
import robhopkins.wc.iam.user.exception.UserNotFoundException;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * This represents the users that are managed by the IAM service.
 */
public interface Users {

    User get(UserId id) throws UserNotFoundException, IAMException;

    User get(Username username) throws UserNotFoundException, IAMException;

    Collection<User> getAll() throws IAMException;

    User add(User user) throws IAMException;

    @Deprecated
    Collection<User> find(Predicate<User> filter) throws IAMException;
}
