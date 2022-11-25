package pl.pollub.gameslibrary.Exceptions.Exceptions;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException() {
        super("Szukany użytkownik nie istnieje.");
    }
}