package pl.pollub.gameslibrary.Exceptions.Exceptions;

public class RoleNotFoundException extends Throwable {
    public RoleNotFoundException() {
        super("Role does not exist.");
    }
}
