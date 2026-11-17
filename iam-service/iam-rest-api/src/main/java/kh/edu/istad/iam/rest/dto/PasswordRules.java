package kh.edu.istad.iam.rest.dto;

public class PasswordRules {

    public static final String COMPLEXITY = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{12,128}$";
    public static final String MESSAGE =
            "must be 12-128 characters with upper-case, lower-case, digit and symbol";

    private PasswordRules() {
    }
}
