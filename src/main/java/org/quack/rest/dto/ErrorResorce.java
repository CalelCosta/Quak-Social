package org.quack.rest.dto;

import lombok.Data;

@Data
public class ErrorResorce {
    private String field;
    private String message;

    public ErrorResorce(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
