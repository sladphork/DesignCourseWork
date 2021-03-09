package robhopkins.wc.iam.user.db;

import robhopkins.wc.iam.user.domain.UserId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "iam_values")
public class HibernateValue {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "iam_value")
    private String value;

    public HibernateValue() {

    }

    HibernateValue(final UserId id, final String value) {
        setId(id.toString());
        setValue(value);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void clear() {
        setId(null);
        setValue(null);
    }
}
