package dataaccess;

public class NameTakenException extends MissingDataException {
    public NameTakenException(String message) {
        super(message);
    }
}
