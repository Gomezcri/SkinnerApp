package com.example.skinnerapp.Model;

public class LoginUsuarioRequest {
    private String email;
    private String password;

    public LoginUsuarioRequest(String usuario, String pass) {
        this.email = usuario;
        this.password = pass;

    }

}