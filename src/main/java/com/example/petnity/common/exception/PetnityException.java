package com.example.petnity.common.exception;

import com.example.petnity.common.Constants;
import org.springframework.http.HttpStatus;

public class PetnityException extends Exception {
    private static final long serialVersionUID = 136287694L;

    private Constants.ExceptionClass exceptionClass;
    private HttpStatus httpStatus;

    public PetnityException(Constants.ExceptionClass exceptionClass, HttpStatus httpStatus, String message) {
        super(exceptionClass.toString() + message);
        this.exceptionClass = exceptionClass;
        this.httpStatus = httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

    public String getHttpStatusType() {
        return httpStatus.getReasonPhrase();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }


}
