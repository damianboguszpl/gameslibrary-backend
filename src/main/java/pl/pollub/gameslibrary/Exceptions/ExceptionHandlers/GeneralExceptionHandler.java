package pl.pollub.gameslibrary.Exceptions.ExceptionHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;
import pl.pollub.gameslibrary.Exceptions.Exceptions.IncorrectRequestDataException;

@ControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler(IncorrectRequestDataException.class)
    public ResponseEntity<DetailedResponse> onIncorrectRequestDataException(IncorrectRequestDataException e) {
        String message = "Zapytanie nie zawiera poprawnych danych.";
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new DetailedResponse("INCORRECT_REQUEST_DATA", message, null));
    }
}
