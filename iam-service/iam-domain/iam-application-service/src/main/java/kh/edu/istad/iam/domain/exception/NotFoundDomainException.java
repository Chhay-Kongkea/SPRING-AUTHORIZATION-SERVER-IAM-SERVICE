package kh.edu.istad.iam.domain.exception;

import kh.edu.istad.common.domain.exception.DomainException;

public class NotFoundDomainException extends DomainException {

    public NotFoundDomainException(String message) {
        super(message);
    }
}