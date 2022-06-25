package pl.estimateplus.model;

import org.mindrot.jbcrypt.BCrypt;


public class Security {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String candidate, String hashed) {
        if (BCrypt.checkpw(candidate, hashed)) {
            return true;
        } else {
            return false;
        }
    }
}
