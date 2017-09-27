package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.util.exception.DuplicateValueException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalArgumentException(bean + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
//      http://stackoverflow.com/a/32728226/548473
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.getId() != id) {
            throw new IllegalArgumentException(bean + " must be with id=" + id);
        }
    }

    //  http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static Map<String, String> causesByErrors = new HashMap<>();
    static {
        causesByErrors.put("DuplicateEmail", "users_unique_email_idx");
        causesByErrors.put("DuplicateDateTime", "meals_unique_user_datetime_idx");
    }

    public static void throwThoughtfully(Exception e) {
        String message = getRootCause(e).getLocalizedMessage();
        if ( message.contains(causesByErrors.get("DuplicateEmail")) ) {
            throw new DuplicateValueException(message.substring(message.lastIndexOf("\n")), "error.DuplicateEmail");
        }
         else if ( message.contains(causesByErrors.get("DuplicateDateTime")) ) {
            throw new DuplicateValueException(message.substring(message.lastIndexOf("\n")), "error.DuplicateDateTime");
            }
        else {
            //throw e; //не работает, ругается на "Unhandled exception type Exception", хотя в catch(в используемом методе) не ругался...
            throw new RuntimeException(e);
        }
    }

}