package utilJwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;

/**
 * JwtManager es la clase que se utiliza para la creacion
 * y validacion de un JWT
 */
public class JwtManager {

  private static JwtManager instance;

  /*
   * Estas constantes se utilizan para establecer
   * atributos en la carga util de un JWT
   */
  private final String USER_ID = "userId";
  private final String SUPERUSER = "superuser";
  
  /*
   * La fecha de emision se utiliza para establecer el tiempo en el
   * que se crea un JWT y la fecha de expiracion se utiliza para
   * establecer el tiempo de expiracion de un JWT 
   */
  private Date dateIssue;
  private Date expirationDate;

  /*
   * El valor de esta constante se utiliza para calcular la fecha
   * de expiracion de un JWT
   */
  private final int OFFSET = 900000;

  private JwtManager() {
    /*
     * Con el patron de diseño Singleton, el metodo constructor
     * de la clase que lo implementa, se ejecuta una unica vez.
     * De esta manera, y en este caso, se instancia una unica
     * vez, dos veces la clase Date. Esto es que se crean una
     * unica vez, dos objetos de tipo Date. La referencia de
     * cada uno de los dos se almacena en una variable de tipo
     * por referencia de tipo Date.
     */
    dateIssue = new Date();
    expirationDate = new Date();
  }

  /**
   * @return referencia a un objeto de tipo JwtManager
   */
  public static JwtManager getInstance() {
    
    if (instance == null) {
      instance = new JwtManager();
    }

    return instance;
  }

  /**
   * Crea un JWT con el ID y el permiso de un usuario
   * 
   * @param userId ID de un usuario
   * @param superuserPermission permiso de un usuario
   * @param SecretKey clave secreta con la que se firma un JWT
   * @return referencia a un objeto de tipo String que contiene un JWT
   */
  public String createJwt(int userId, boolean superuserPermission, String secretKey) {
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

}