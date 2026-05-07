package app.quadras.entity;

public enum TipoUsuario {
    SYSJEGG_ADMIN("sys-jegg_admin"),
    SYSJEGG_USER("sys-jegg_user");

    private String role;

    TipoUsuario(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
