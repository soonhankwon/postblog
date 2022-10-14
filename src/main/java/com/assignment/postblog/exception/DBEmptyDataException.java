package com.assignment.postblog.exception;

import lombok.Getter;

@Getter
public class DBEmptyDataException extends RuntimeException {

    private String message;

    public DBEmptyDataException(String message) {
        this.message = message;

    }
}
