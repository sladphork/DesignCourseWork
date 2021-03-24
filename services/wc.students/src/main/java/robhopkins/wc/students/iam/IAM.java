package robhopkins.wc.students.iam;

import robhopkins.wc.students.iam.exception.ForbiddenException;
import robhopkins.wc.students.iam.exception.UnauthenticatedException;

public interface IAM {
    Token validate(String role) throws UnauthenticatedException, ForbiddenException;
}
