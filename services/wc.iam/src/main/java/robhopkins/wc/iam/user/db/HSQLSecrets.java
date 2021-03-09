package robhopkins.wc.iam.user.db;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import robhopkins.wc.iam.request.exception.AuthenticationException;
import robhopkins.wc.iam.user.Secrets;
import robhopkins.wc.iam.user.User;
import robhopkins.wc.iam.user.Secret;

import java.util.Arrays;
import java.util.Objects;

public final class HSQLSecrets extends Secrets {

    private final SessionFactory sessionFactory;
    public HSQLSecrets() {
        this.sessionFactory = sessionFactory();
    }

    @Override
    public Secret getFor(final User user) {
        return new HSQLSecret(user, sessionFactory);
    }

    private SessionFactory sessionFactory() {
        return metadata().getSessionFactoryBuilder().build();
    }

    private Metadata metadata() {
        return (new MetadataSources(registry())).getMetadataBuilder().build();
    }

    private StandardServiceRegistry registry() {
        return new StandardServiceRegistryBuilder().configure().build();
    }

    private static final class HSQLSecret implements Secret {
        private final SessionFactory factory;
        private final User user;

        HSQLSecret(final User user, final SessionFactory factory) {
            this.factory = factory;
            this.user = user;
        }

        @Override
        public void check(final byte[] toCheck) throws AuthenticationException {
            try (Session session = factory.openSession()) {
                final HibernateValue value = session.get(HibernateValue.class, user.id().toString());
                if (Objects.isNull(value) ) {
                    throw new AuthenticationException(user.username());
                }
                final byte[] checked = value.getValue().getBytes();
                value.clear();
                if (!Arrays.equals(checked, toCheck)) {
                    throw new AuthenticationException(user.username());
                }
            } catch (HibernateException e) {
                throw new AuthenticationServerException(e);
            }
        }

        @Override
        public void set() {
            // TODO: Generate a random password and set it to the user.
            set("password".getBytes());
        }

        @Override
        public void set(final byte[] value) {
            try (Session session = factory.openSession()) {
                session.beginTransaction();
                session.save(new HibernateValue(user.id(), new String(value)));
                // Note, this is stupid but it's what it does :(
                session.getTransaction().commit();
            } catch (HibernateException e) {
                // Log?
            }
        }
    }


    private static final class AuthenticationServerException extends AuthenticationException {
        AuthenticationServerException(final Exception cause) {
            super(String.format("Unable to authenicate user: %s", cause.getCause()));
        }

        @Override
        public int status() {
            return 500;
        }
    }
}
