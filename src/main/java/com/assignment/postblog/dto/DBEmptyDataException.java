package com.assignment.postblog.dto;

import lombok.Getter;

@Getter
public class DBEmptyDataException extends RuntimeException {

    private String message;

    public DBEmptyDataException(String message) {
        this.message = message;

    }
}
