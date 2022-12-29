package utilPermission;

/**
 * PermissionResponse es la clase que se utiliza para la respuesta a un intento
 * por parte del usuario cliente de acceder a una pagina web a la que no debe
 * acceder, debido a que no tiene el permiso de super usuario (administrador)
 */
public class PermissionResponse {

  /*
   * El unico mensaje que puede surgir al intentar el usuario cliente
   * acceder a una pagina web a la que no debe acceder, debido a que no
   * tiene el permiso de super usuario (administrador), es el de "Acceso
   * no autorizado"
   */
  private static final String message = "Acceso no autorizado";

  public PermissionResponse() {

  }

  public String getMessage() {
    return message;
  }

}
