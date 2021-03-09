package robhopkins.wc.iam.user.db;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import robhopkins.wc.iam.user.Users;
import robhopkins.wc.iam.user.UsersFactory;

public class HSQLUsersFactory extends UsersFactory {
    @Override
    public Users create() {
        return new HSQLUsers(sessionFactory());
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
}
