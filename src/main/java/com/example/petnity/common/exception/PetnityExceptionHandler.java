package com.example.petnity.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class PetnityExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(PetnityExceptionHandler.class);
//
//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<Map<String, String>> ExceptionHandler(Exception e) {
//        HttpHeaders responseHeaders = new HttpHeaders();
//        //responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
//        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
//
//        LOGGER.info("Advice 내 ExceptionHandler 호출");
//
//        Map<String, String> map = new HashMap<>();
//        map.put("error type", httpStatus.getReasonPhrase());
//        map.put("code", Integer.toString(httpStatus.value()));
//        map.put("message", "에러 발생");
//
//        return new ResponseEntity<>(map, responseHeaders, httpStatus);
//    }
//
//    @ExceptionHandler(value = PetnityException.class)
//    public ResponseEntity<Map<String, String>> ExceptionHandler(PetnityException e) {
//        HttpHeaders responseHeaders = new HttpHeaders();
//        Map<String, String> map = new HashMap<>();
//        map.put("error type", e.getHttpStatusType());
//        map.put("code", Integer.toString(e.getHttpStatusCode()));
//        map.put("message", e.getMessage());
//
//        return new ResponseEntity<>(map, responseHeaders, e.getHttpStatus());
//    }
}
