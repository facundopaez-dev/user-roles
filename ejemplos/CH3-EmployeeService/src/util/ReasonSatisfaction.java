package util;

/**
 * ReasonSatisfaction es el enum que contiene las causas (como constantes
 * de enumeracion) por las cuales una operacion solicitada mediante una
 * peticion HTTP, es realizada satisfactoriamente
 */
public enum ReasonSatisfaction {
  ACCOUNT_ACTIVATED("Cuenta activada"),
  SUCCESSFULLY_REGISTERED_USER("Usuario registrado, revise su casilla de correo electrónico para activar su cuenta");

  private final String reason;

  private ReasonSatisfaction(String reason) {
    this.reason = reason;
  }

  public String getReason() {
    return reason;
  }

}
