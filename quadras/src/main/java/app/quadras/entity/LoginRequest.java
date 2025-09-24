package app.quadras.entity;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String email;
    private String senha;
}