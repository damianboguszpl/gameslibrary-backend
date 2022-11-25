package pl.pollub.gameslibrary.Exceptions.ExceptionHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;
import pl.pollub.gameslibrary.Exceptions.Exceptions.*;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<DetailedResponse> onUserNotFoundException() {
        String message = "User does not exist.";
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new DetailedResponse("USER_NOT_FOUND", message, null));
    }
}
