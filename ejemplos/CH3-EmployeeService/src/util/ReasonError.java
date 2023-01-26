package util;

/**
 * ReasonError es el enum que contiene las causas (como constantes
 * de enumeracion) por las cuales no se puede satisfacer una
 * peticion HTTP
 */
public enum ReasonError {
  USERNAME_OR_PASSWORD_INCORRECT("Nombre de usuario o contraseña incorrectos"),
  SESSION_EXPIRED("Sesión expirada"),
  UNAUTHORIZED_ACCESS("Acceso no autorizado"),
  RESOURCE_NOT_FOUND("Recurso no encontrado"),
  MULTIPLE_SESSIONS("No es posible tener mas de una sesión abierta simultáneamente");

  private final String reason;

  private ReasonError(String reason) {
    this.reason = reason;
  }

  public String getReason() {
    return reason;
  }

}
