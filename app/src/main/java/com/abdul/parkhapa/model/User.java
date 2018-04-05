package com.abdul.parkhapa.model;

/**
 * Created by abduljama on 11/21/17.
 */

public class User {

    String name;
    String email;
    String password;
    String role;

    public User( String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
