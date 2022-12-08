package utilPermission;

/**
 * PermissionResponse es la clase que se utiliza para la respuesta a un intento
 * por parte del usuario cliente de acceder a una pagina a la que no debe acceder,
 * debido a que no tiene el permiso de super usuario (administrador)
 */
public class PermissionResponse {

  /*
   * El unico error que puede surgir al intentar el usuario cliente
   * acceder a una pagina a la que no debe acceder, debido a que no tiene
   * el permiso de super usuario (administrador), es el de "Acceso no
   * autorizado"
   */
  private final String errorMessage = "Acceso no autorizado";

  public PermissionResponse() {

  }

  public String getErrorMessage() {
    return errorMessage;
  }

}
