package com.example.skinnerapp.Model;

public class AnalizarImagenRequest {
    private String image;
    private String user;
    private String body;
    private String section;

    public AnalizarImagenRequest(String image, String body,String section,String user) {
        this.image = image;
        this.body = body;
        this.user = user;
        this.section = section;
    }
}

