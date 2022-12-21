package utilJwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

/**
 * JwtManager es la clase que se utiliza para la creacion
 * y validacion de un JWT
 */
public class JwtManager {

  /*
   * Estas constantes se utilizan para establecer
   * atributos en la carga util de un JWT
   */
  private static final String USER_ID = "userId";
  private static final String SUPERUSER = "superuser";
  
  /*
   * La fecha de emision se utiliza para establecer el tiempo en el
   * que se crea un JWT y la fecha de expiracion se utiliza para
   * establecer el tiempo de expiracion de un JWT 
   */
  private static Date dateIssue = new Date();
  private static Date expirationDate = new Date();

  /*
   * Esta constante se utiliza para calcular la fecha de expiracion
   * de un JWT y su valor representa 15 minutos en milisegundos
   */
  private static final int OFFSET = 900000;

  /*
   * Estas constantes se utilizan para obtener el ID de usuario
   * y el permiso de administrador (super usuario) contenidos
   * en la carga util de un JWT
   */
  private static final String COMMA = ",";
  private static final String TWO_POINTS = ":";
  private static final String USER_ID_KEY = "\"userId\"";
  private static final String SUPERUSER_KEY = "\"superuser\"";

  /*
   * El metodo constructor tiene el modificador de acceso 'private'
   * para que ningun programador trate de instanciar esta clase
   * desde afuera, ya que todos los metodos publicos de la misma
   * son estaticos, con lo cual, no se requiere una instancia de
   * esta clase para invocar a sus metodos publicos
   */
  private JwtManager() {

  }

  /**
   * Crea un JWT con el ID y el permiso de un usuario, una fecha
   * de emision y una fecha de expiracion
   * 
   * @param userId ID de un usuario
   * @param superuserPermission permiso de un usuario
   * @param secretKey clave secreta con la que se firma un JWT
   * @return referencia a un objeto de tipo String que contiene un JWT
   */
  public static String createJwt(int userId, boolean superuserPermission, String secretKey) {
    /*
     * Asigna el tiempo actual a la fecha de emision
     */
    dateIssue.setTime(System.currentTimeMillis());

    /*
     * Establece el tiempo de la fecha de expiracion como la
     * suma entre el tiempo de la fecha de emision y un desplazamiento
     */
    expirationDate.setTime(dateIssue.getTime() + OFFSET);

    JWTCreator.Builder jwtCreator = JWT.create();
    jwtCreator.withClaim(USER_ID, userId);
    jwtCreator.withClaim(SUPERUSER, superuserPermission);
    jwtCreator.withIssuedAt(dateIssue);
    jwtCreator.withExpiresAt(expirationDate);

    return jwtCreator.sign(Algorithm.HMAC256(secretKey));
  }

  /**
   * Comprueba que un JWT sea valido, esto es que la firma de un JWT coincide
   * con los datos de su encabezado y carga util, y que el JWT no ha expirado
   * 
   * @param jwt
   * @param secretKey clave secreta a partir de la cual se firma un JWT
   * @return true si el JWT dado es valido, false en caso contrario
   */
  public static boolean validateJwt(String jwt, String secretKey) {
    JWTVerifier jwtVerifier = buildJwtVerifier(secretKey);
    boolean result = false;

    try {
      jwtVerifier.verify(jwt);
      result = true;      
    } catch (JWTVerificationException e) {
      e.printStackTrace();
    }
    
    return result;
  }

  /**
   * Crea y retorna una instancia de JWTVerifier
   * 
   * @param secretKey clave secreta que se utiliza para firmar un JWT
   * @return una referencia a un objeto de tipo JWTVerifier
   */
  private static JWTVerifier buildJwtVerifier(String secretKey) {
    Verification verification = JWT.require(Algorithm.HMAC256(secretKey));
    return verification.build();
  }

  /**
   * Recupera el ID de usuario contenido en la carga util de un JWT
   * 
   * @param jwt
   * @return entero que contiene el ID de usuario contenido en la
   * carga util de un JWT
   */
  public static int retrieveUserId(String jwt, String secretKey) {
    String payload = getDecodedPayload(jwt, secretKey);

    /*
     * Crea un arreglo de tipo String que contiene cada uno de
     * los pares clave:valor de la carga util de un JWT
     * dividiendola por la coma
     */
    String[] keyValuePairs = payload.split(COMMA);
    String[] pair = null;
    int userId = 0;

    /*
     * Recorre cada uno de los pares clave:valor de la carga util
     * de un JWT hasta obtener el ID de usuario
     */
    for (String currentPair : keyValuePairs) {
      /*
       * Crea un arreglo de tipo String que contiene la clave
       * y el valor de un par clave:valor dividiendolo por los
       * dos puntos
       */
      pair = currentPair.split(TWO_POINTS);

      /*
       * Si la clave es 'userId', se obtiene el valor de la misma,
       * el cual, es el ID de usuario contenido en la carga util
       * de un JWT
       */
      if (pair[0].equals(USER_ID_KEY)) {
        userId = Integer.parseInt(pair[1]);
        break;
      }

    }

    return userId;
  }

  /**
   * Recupera el permiso de administrador (super usuario) contenido en la
   * carga util de un JWT
   * 
   * @param jwt
   * @param secretKey clave secreta con la que se firma un JWT
   * @return true si el valor asociado a la clave 'superuser' es true, false
   * si el valor asociado a la clave 'superuser' es false
   */
  public static boolean retrieveSuperuser(String jwt, String secretKey) {
    String payload = getDecodedPayload(jwt, secretKey);
    return Boolean.parseBoolean(retrieveValueKey(SUPERUSER_KEY, payload));
  }

  /**
   * Obtiene el valor asociado a una clave dada de la carga util de
   * un JWT
   * 
   * @param key clave de la que se quiere obtener su valor asociado
   * de la carga util de un JWT
   * @param payload carga util decodificada de un JWT
   * @return referencia a un objeto de tipo String que contiene el valor
   * asociado a una clave dada de la carga util decodificada de un JWT
   */
  private static String retrieveValueKey(String key, String payload) {
    /*
     * Elimina las llaves de apertura y cierre de la carga util
     * decodificada de un JWT para que las mismas no esten
     * presentes en el valor devuelto por este metodo.
     * 
     * Si no se eliminan las llaves de apertura y cierre, y
     * la clave provista es 'superuser', el valor devuelto
     * por este metodo contiene la cadena 'true' o 'false'
     * seguida de la llave de cierre, lo cual, provoca que
     * el metodo retrieveSuperuser() retorne valores
     * booleanos incorrectos.
     * 
     * Por lo tanto, eliminar las llaves de apertura y cierre
     * garantiza el correcto funcionamiento de los metodos
     * que invoquen a este metodo.
     */
    payload = removeBraces(payload);

    /*
     * Crea un arreglo de tipo String que contiene cada uno de
     * los pares clave:valor de la carga util de un JWT
     * dividiendola por la coma
     */
    String[] keyValuePairs = payload.split(COMMA);
    String[] pair = null;
    String value = null;

    /*
     * Recorre cada uno de los pares clave:valor de la carga util
     * de un JWT hasta obtener el valor asociado a la clave dada
     */
    for (String currentPair : keyValuePairs) {
      /*
       * Crea un arreglo de tipo String que contiene la clave
       * y el valor de un par clave:valor dividiendolo por los
       * dos puntos
       */
      pair = currentPair.split(TWO_POINTS);

      /*
       * Si la clave es igual a la clave provista, se obtiene el valor
       * asociado a la misma, el cual, puede ser el ID de usuario o
       * el permiso de administrador (super usuario) dependiendo
       * de lo que se desee recuperar de la carga util, decodificada
       * de un JWT, mediante la clave provista a este metodo
       */
      if (pair[0].equals(key)) {
        value = pair[1];
        break;
      }

    }

    return value;
  }

  /**
   * Obtiene la carga util decodificada de un JWT
   * 
   * @param jwt
   * @return referencia a un objeto de tipo String que contiene la carga
   * util decodificada de un JWT
   */
  private static String getDecodedPayload(String jwt, String secretKey) {
		JWTVerifier jwtVerifier = buildJwtVerifier(secretKey);
		DecodedJWT decodedJwt = jwtVerifier.verify(jwt);
		byte[] decoded = Base64.getDecoder().decode(decodedJwt.getPayload());

		return new String(decoded, StandardCharsets.UTF_8);
	}

  /**
   * Elimina las llaves de apertura y cierre de la carga util decodificada
   * de un JWT
   * 
   * @param payload carga util decodificada de un JWT
   * @return referencia a un objeto de tipo String que contiene la carga
   * util decodificada de un JWT, pero sin las llaves de apertura y cierre
   */
  private static String removeBraces(String payload) {
    /*
     * Reemplaza las llaves de apertura y cierre de la carga util
     * decodificada de un JWT, por un espacio en blanco
     */
    return payload.replaceAll("[\\{]|[\\}]", "");
  }

}
