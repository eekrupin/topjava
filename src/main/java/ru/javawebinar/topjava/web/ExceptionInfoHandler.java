package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.DuplicateValueException;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.StringJoiner;

@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger LOG = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    private final MessageSource messageSource;

    @Autowired
    public ExceptionInfoHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY) //422
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        return logAndGetErrorInfo(req, e, true);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)  //422
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class, DuplicateValueException.class})
    @ResponseBody
    public ErrorInfo bindError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  //500
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true);
    }

    private ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception ex, boolean logException) {
        Locale locale = LocaleContextHolder.getLocale();
        Throwable rootCause = ValidationUtil.getRootCause(ex);
        if (logException) {
            LOG.error("Exception at request " + req.getRequestURL(), rootCause);
        } else {
            LOG.warn("Exception at request " + req.getRequestURL() + ": " + rootCause.toString());
        }

        String cause;
        String detail;
        if (ex instanceof BindException) {
            cause = messageSource.getMessage("error.fill", null, locale);
            detail = getErrorsFromBindingResult( ((BindException) ex).getBindingResult() );
        }
        else if (ex instanceof DuplicateValueException) {
            cause = messageSource.getMessage( ((DuplicateValueException)ex).type, null, locale );
            detail = ex.getLocalizedMessage();
        }
        else {
            cause = ex.getClass().getSimpleName();
            detail = ex.getLocalizedMessage();
        }

        return new ErrorInfo(req.getRequestURL(), cause, detail);
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