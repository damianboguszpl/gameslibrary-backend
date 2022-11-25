package pl.pollub.gameslibrary.Exceptions.Exceptions;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException() {
        super("User does not exist.");
    }
}