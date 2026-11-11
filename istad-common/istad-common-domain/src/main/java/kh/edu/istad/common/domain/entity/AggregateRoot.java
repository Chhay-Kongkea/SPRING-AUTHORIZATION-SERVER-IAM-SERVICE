package kh.edu.istad.common.domain.entity;

public abstract class AggregateRoot<ID> extends BaseEntity<ID> {

    protected AggregateRoot(ID id) {
        super(id);
    }
}