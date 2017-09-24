package ru.javawebinar.topjava.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.util.exception.DuplicateValueException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.StringJoiner;

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

    public static Boolean isCauseWithDuplicateEmail(Exception e) {
        return e.getCause().getCause().getLocalizedMessage().contains("users_unique_email_idx");
    }

    public static void throwThoughtfully(Exception e) {
        if (isCauseWithDuplicateEmail(e)) {
            String message = e.getCause().getCause().getLocalizedMessage();
            throw new DuplicateValueException( message.substring(message.lastIndexOf("\n")), "error.DuplicateEmail");
        }else {
            //throw e; //не работает, ругается на "Unhandled exception type Exception", хотя в catch(в используемом методе) не ругался...
            throw new RuntimeException(e);
        }
    }

}