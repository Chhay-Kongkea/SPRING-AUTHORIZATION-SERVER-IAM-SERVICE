package kh.edu.istad.iam.domain.exception;

import kh.edu.istad.common.domain.exception.DomainException;

public class InvalidCredentialsDomainException extends DomainException {

    public InvalidCredentialsDomainException(String message) {
        super(message);
    }
}