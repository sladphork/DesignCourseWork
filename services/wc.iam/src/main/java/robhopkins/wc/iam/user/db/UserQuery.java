package robhopkins.wc.iam.user.db;

import org.hibernate.Session;
import robhopkins.wc.iam.user.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

final class UserQuery {
    static UserQuery using(final Session session) {
        return new UserQuery(session);
    }

    private final Session session;
    private final String[] where = new String[2];
    private UserQuery(final Session session) {
        this.session = session;
    }

    UserQuery where(final String field, final Object value) {
        where[0] = field;
        where[1] = value.toString();

        return this;
    }

    List<User> execute() {
        final CriteriaBuilder builder = session.getCriteriaBuilder();
        final CriteriaQuery<HibernateUser> criteria = builder.createQuery(HibernateUser.class);
        final Root<HibernateUser> from = criteria.from(HibernateUser.class);

        if (null != where[0]) {
            criteria.where(builder.equal(from.get(where[0]), where[1]));
        }

        return session.createQuery(criteria).list().stream()
            .map(HibernateUser::toUser)
            .collect(Collectors.toList());
    }
}
