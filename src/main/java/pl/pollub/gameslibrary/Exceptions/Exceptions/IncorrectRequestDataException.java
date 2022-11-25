package pl.pollub.gameslibrary.Exceptions.Exceptions;

public class IncorrectRequestDataException extends Throwable {
    public IncorrectRequestDataException() {
        super("Zapytanie nie zawiera poprawnych danych.");
    }
}