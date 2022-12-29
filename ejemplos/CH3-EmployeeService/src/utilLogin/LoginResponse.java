package utilLogin;

/**
 * LoginResponse es la clase que se utiliza para la respuesta a un intento
 * de inicio de sesion del usuario, tenga este o no permiso de super usuario
 * (administrador)
 */
public class LoginResponse {

  /*
   * Los posibles mensajes que pueden surgir en un intento de inicio de
   * sesion son los siguientes:
   * - Nombre de usuario o contraseña incorrectos.
   * - Acceso no autorizado.
   * 
   * Estos mensajes estan definidos como constantes de enumeracion en
   * el enum LoginStatus.
   */
  private String message;

  public LoginResponse(LoginStatus loginStatus) {
    message = loginStatus.getReason();
  }

  public String getMessage() {
    return message;
  }

}
