package org.quack.rest.dto;

import lombok.Data;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ResponseError {

    public static final int UNPROCESSABLE_ENTITY_STATUS = 422;

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

    public Response withStatusCode(int code) {
        return Response.status(code).entity(this).build();
    }
}
