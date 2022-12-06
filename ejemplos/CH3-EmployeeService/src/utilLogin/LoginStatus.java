package utilLogin;

/**
 * LoginStatus es el enum que contiene las posibles respuestas a
 * un intento de inicio de sesion del usuario, como constantes de
 * enumeracion
 */
public enum LoginStatus {
  USERNAME_OR_PASSWORD_INCORRECT("Nombre de usuario o contraseña incorrectos"),
  UNAUTHORIZED_ACCESS("Acceso no autorizado");

  private final String reason;

  private LoginStatus(String reason) {
    this.reason = reason;
  }

  public String getReason() {
    return reason;
  }
  
}
