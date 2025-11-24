package app.quadras.entity;

public enum TipoUsuario {
    ADMIN("admin"),
    USER("user");

    private String role;

    TipoUsuario(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
