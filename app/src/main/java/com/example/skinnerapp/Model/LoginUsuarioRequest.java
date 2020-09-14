package com.example.skinnerapp.Model;

public class LoginUsuarioRequest {
    private String email;
    private String password;
    private String token;

    public LoginUsuarioRequest(String usuario, String pass, String token) {
        this.email = usuario;
        this.password = pass;
        this.token = token;
    }

}