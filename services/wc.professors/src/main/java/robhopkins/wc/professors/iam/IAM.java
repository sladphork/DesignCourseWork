package robhopkins.wc.professors.iam;

import robhopkins.wc.professors.iam.exception.ForbiddenException;
import robhopkins.wc.professors.iam.exception.UnauthenticatedException;

public interface IAM {
    Token validate(String role) throws UnauthenticatedException, ForbiddenException;
}
