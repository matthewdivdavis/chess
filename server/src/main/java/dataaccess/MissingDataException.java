package dataaccess;

public class MissingDataException extends DataAccessException {
    public MissingDataException(String message) {
        super(message);
    }
    public MissingDataException(String message, Throwable ex){
        super(message, ex);
    }
}
