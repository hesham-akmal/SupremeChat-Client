package network_data;

import java.io.Serializable;

public class AuthUser implements Serializable {


   private String username ;
   private String password ;
   private String IP;

    public AuthUser(String username, String password, String IP) {
        this.username = username;
        this.password = password;
        this.IP = IP;
    }
    @Override
    public String toString() {
        return username;
    }

}
