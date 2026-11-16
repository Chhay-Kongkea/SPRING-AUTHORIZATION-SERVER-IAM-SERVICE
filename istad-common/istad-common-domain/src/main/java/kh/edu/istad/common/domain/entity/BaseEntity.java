package kh.edu.istad.common.domain.entity;

import java.util.Objects;

public abstract class BaseEntity<ID> {

    private ID id;

    protected BaseEntity(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    protected void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        BaseEntity<?> that = (BaseEntity<?>) object;

        // Two new entities without IDs are not equal.
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        // Keep the hash stable while an entity receives its database ID.
        return Objects.hash(getClass());
    }
}