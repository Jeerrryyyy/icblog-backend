package io.ic1101.icblog.api.user.exception.custom;

public class UserOrRoleNotFoundException extends RuntimeException {

    public UserOrRoleNotFoundException() {
        super("Could not find a role or a user matching your values!");
    }
}
