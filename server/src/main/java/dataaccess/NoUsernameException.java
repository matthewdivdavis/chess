package dataaccess;

public class NoUsernameException extends DataAccessException {
    public NoUsernameException(String message) {
        super(message);
    }
    public NoUsernameException(String message, Throwable ex){
        super(message, ex);
    }
}
