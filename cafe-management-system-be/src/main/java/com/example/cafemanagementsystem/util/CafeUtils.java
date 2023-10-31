package com.example.cafemanagementsystem.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public final class CafeUtils {

    private CafeUtils() {

    }

    public static ResponseEntity<String> getResponseEntity(final String responseMessage, final HttpStatus httpStatus) {
        return new ResponseEntity<String>("{\"message\":\"" + responseMessage + "\"}", httpStatus);
    }

    public static String getUUID() {
        Date date = new Date();
        long time = date.getTime();
        return String.format("Bill-%s", time);
    }

}
