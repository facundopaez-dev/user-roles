package utilLogin;

import model.User;

/**
 * LoginResponse es la clase que se utiliza para la respuesta a un intento
 * de inicio de sesion del usuario, tenga este o no permiso de super usuario
 * (administrador)
 */
public class LoginResponse {

  /*
   * Los posibles errores que pueden surgir en un intento de inicio de
   * sesion son los siguientes:
   * - Nombre de usuario o contraseña incorrectos.
   * - Acceso no autorizado.
   * 
   * Estos errores estan definidos como constantes de enumeracion en
   * el enum LoginStatus.
   */
  private String errorMessage;

  /*
   * En caso de que el inicio de sesion del usuario sea satisfactorio
   * (debido a que no hubo ningun error), se envia el usuario como
   * respuesta a la aplicacion del frontend
   */
  private User user;

  public LoginResponse(LoginStatus loginStatus) {
    errorMessage = loginStatus.getReason();
  }

  public LoginResponse(User user) {
    this.user = user;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }


  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

}
