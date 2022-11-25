package pl.pollub.gameslibrary.Exceptions.ExceptionHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.pollub.gameslibrary.Exceptions.Exceptions.RoleNotFoundException;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;

@ControllerAdvice
public class RoleExceptionHandler {
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<DetailedResponse> onRoleNotFoundException() {
        String message = "Role does not exist.";
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new DetailedResponse("ROLE_NOT_FOUND", message, null));
    }
}