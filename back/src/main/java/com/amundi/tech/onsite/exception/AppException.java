package com.amundi.tech.onsite.exception;

public class AppException extends Exception {

    public static final String EMPTY_PARAM = "empty param";
    public static final String EMPTY_SITE = "empty site";
    public static final String EMPTY_USER = "empty user";
    public static final String EMPTY_USER_ROLES = "empty user role list";
    public static final String SITE_DOESNT_EXIST = "site % does not exist";
    public static final String RESTAURANT_DOESNT_EXIST = "restaurant %s doesn not exist";
    public static final String CURRENT_USER_CANNOT_REMOVE_ADMIN_ROLE = "current user cannot remove his own admin role";

    public AppException() {
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(Throwable cause) {
        super(cause);
    }

    public AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
