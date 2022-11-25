package pl.pollub.gameslibrary.Exceptions.ExceptionHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.pollub.gameslibrary.Models.Utility.DetailedUserResponse;
import pl.pollub.gameslibrary.Exceptions.Exceptions.*;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<DetailedUserResponse> onUserNotFoundException() {
        String message = "Szukany użytkownik nie istnieje.";
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new DetailedUserResponse("USER_NOT_FOUND", message, null));
    }
}
