package ru.javawebinar.topjava.util.exception;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import java.util.Locale;
import java.util.StringJoiner;

public class ErrorInfo {
    private final String url;
    private final String cause;
    private final String detail;

    public ErrorInfo(CharSequence url, Throwable ex, MessageSource messageSource, Locale locale) {
        this.url = url.toString();
        if (ex instanceof BindException) {
            this.cause = messageSource.getMessage("error.fill", null, locale);
            this.detail = getErrorsFromBindingResult( ((BindException) ex).getBindingResult() );
        }
        else if (ex instanceof DuplicateValueException) {
            this.cause = messageSource.getMessage( ((DuplicateValueException)ex).type, null, locale );
            this.detail = ex.getLocalizedMessage();
        }
        else {
            this.cause = ex.getClass().getSimpleName();
            this.detail = ex.getLocalizedMessage();
        }
    }

    public static String getErrorsFromBindingResult(BindingResult result) {
        StringJoiner joiner = new StringJoiner("<br>");
        result.getFieldErrors().forEach(
                fe -> {
                    String msg = fe.getDefaultMessage();
                    if (!msg.startsWith(fe.getField())) {
                        msg = fe.getField() + ' ' + msg;
                    }
                    joiner.add(msg);
                });
        return joiner.toString();
    }

}