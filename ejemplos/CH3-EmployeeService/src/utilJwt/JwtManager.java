package utilJwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Verification;
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

}
