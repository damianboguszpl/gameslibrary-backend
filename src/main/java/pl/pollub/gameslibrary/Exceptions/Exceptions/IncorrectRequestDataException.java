package pl.pollub.gameslibrary.Exceptions.Exceptions;

public class IncorrectRequestDataException extends Throwable {
    public IncorrectRequestDataException() {
        super("Request does not contain required data.");
    }
}