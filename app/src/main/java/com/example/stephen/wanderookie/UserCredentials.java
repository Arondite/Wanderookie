package com.example.stephen.wanderookie;

import java.util.Objects;

/**
 * Created by Stephen on 6/25/2016.
 */
public class UserCredentials {
    private String _Username;
    private String _Password;
    final private String _GuestPassword = "guest";
    final private String _GuestUsername = "guest";

    public String GetUsername(){
        return _Username;
    }
    public void SetUsername(String value){
        _Username = value;
    }
    public void SetPassword(String value){
        _Password = value;
    }

    public UserCredentials(String username, String password){
        _Password = password;
        _Username = username;
    }
    public UserCredentials(){
        _Password = _GuestPassword;
        _Username = _GuestUsername;
    }
    public UserCredentials(UserCredentials uc){
        _Username = uc.GetUsername();
        _Password = uc._Password;
    }

    public boolean MatchPassword(String password){
        return Objects.equals(_Password, password);
    }
}
