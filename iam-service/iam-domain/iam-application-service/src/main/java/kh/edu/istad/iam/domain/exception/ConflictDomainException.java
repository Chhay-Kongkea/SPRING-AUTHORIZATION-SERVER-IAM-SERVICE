package kh.edu.istad.iam.domain.exception;

import kh.edu.istad.common.domain.exception.DomainException;

public class ConflictDomainException extends DomainException {

    public ConflictDomainException(String message) {
        super(message);
    }
}