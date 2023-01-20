package util;

/**
 * ResponseErrorMessage es el enum que contiene los mensajes de error
 * (como constantes de enumeracion) para las peticiones HTTP que por
 * algun motivo no se pueden satisfacer
 */
public enum ResponseErrorMessage {
  USERNAME_OR_PASSWORD_INCORRECT("Nombre de usuario o contraseña incorrectos"),
  SESSION_EXPIRED("Sesión expirada"),
  UNAUTHORIZED_ACCESS("Acceso no autorizado"),
  RESOURCE_NOT_FOUND("Recurso no encontrado");

  private final String message;

  private ResponseErrorMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
