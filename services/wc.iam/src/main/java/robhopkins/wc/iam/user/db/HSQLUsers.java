package robhopkins.wc.iam.user.db;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import robhopkins.wc.iam.request.exception.IAMException;
import robhopkins.wc.iam.user.User;
import robhopkins.wc.iam.user.Users;
import robhopkins.wc.iam.user.domain.UserId;
import robhopkins.wc.iam.user.domain.Username;
import robhopkins.wc.iam.user.exception.UserNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * This class is the {@link Users} implementation that uses the H2 DB for data
 * persistence.
 */
public class HSQLUsers implements Users {

    private final SessionFactory sessionFactory;

    HSQLUsers(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User get(final UserId id) throws UserNotFoundException, IAMException {
        try (final Session session = sessionFactory.openSession()) {
            final List<User> users = UserQuery.using(session)
                .where("id", id)
                .execute();

            if (users.size() == 1) {
                return users.get(0);
            }
            throw new UserNotFoundException(id);

        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    @Override
    public User get(final Username username) throws UserNotFoundException, IAMException {
        try (final Session session = sessionFactory.openSession()) {
            final List<User> users = UserQuery.using(session)
                .where("username", username)
                .execute();

            if (users.size() == 1) {
                return users.get(0);
            }
            throw new UserNotFoundException(username);

        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Collection<User> getAll() throws IAMException {
        try (final Session session = sessionFactory.openSession()) {
            return UserQuery.using(session)
                .execute();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    @Override
    public User add(final User user) throws IAMException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(new HibernateUser(user));
            // Note, this is stupid but it's what it does :(
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new DBException(e);
        }

        return user;
    }


    @Override
    public Collection<User> find(final Predicate<User> filter) throws IAMException {
        return null;
    }

    private static final class DBException extends IAMException {

        DBException(final Exception cause) {
            super(cause.getMessage());
        }

        @Override
        public int status() {
            return 500;
        }
    }
}
