package org.quack.rest.dto;

import javax.validation.ConstraintViolation;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ResponseError {
    private Collection<ErrorResorce> errors;
    private String message;

    public ResponseError(Collection<ErrorResorce> errors, String message) {
        this.errors = errors;
        this.message = message;
    }

    //Esse método mapeia um constraintViolation para um FieldError que será incluso na propriedade da classe.
    public static <T> ResponseError createFromValidate(Set<ConstraintViolation<T>> violations){
        List<ErrorResorce> errors = violations
                .stream()
                .map(cv -> new ErrorResorce(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());

        var message = "Validation Error";
        return new ResponseError(errors, message);
    }

    public Collection<ErrorResorce> getErrors() {
        return errors;
    }

    public void setErrors(Collection<ErrorResorce> errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
