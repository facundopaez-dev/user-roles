package util;

/**
 * SatisfactoryResponse es la clase que se utiliza para indicar, a modo
 * de mensaje, que una peticion HTTP fue satisfactoriamente realizada
 */
public class SatisfactoryResponse {

  /*
   * El mensaje contiene uno de los motivos por los cuales
   * se puede satisfacer una peticion HTTP, los cuales,
   * estan definidos en el enum ReasonSatisfaction
   */
  private String message;

  public SatisfactoryResponse(ReasonSatisfaction reasonSatisfaction) {
    message = reasonSatisfaction.getReason();
  }

  public String getMessage() {
    return message;
  }

}
