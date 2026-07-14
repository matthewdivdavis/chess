package dataaccess;
import model.*;

public class UserDAO {
    public static UserData createUser(String username, String password, String email){
        return new UserData(username, password, email);
    }
}
