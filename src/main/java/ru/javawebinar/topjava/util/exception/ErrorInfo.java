package ru.javawebinar.topjava.util.exception;

public class ErrorInfo {
    private final String url;
    private final String cause;
    private final String detail;

    public ErrorInfo(CharSequence url, String cause, String detail) {
        this.url = url.toString();
        this.cause = cause;
        this.detail = detail;
    }
}