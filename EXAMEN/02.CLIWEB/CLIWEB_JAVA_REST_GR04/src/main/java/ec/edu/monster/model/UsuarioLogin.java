package ec.edu.monster.model;

/**
 * Modelo sencillo para el proceso de login de usuario.
 * Representa los datos que se envían al servidor REST.
 */
public class UsuarioLogin {

    private String usuario;
    private String clave;

    public UsuarioLogin() {
    }

    public UsuarioLogin(String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
