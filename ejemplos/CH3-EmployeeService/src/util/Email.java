package util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Email es la clase que se utiliza para el envio de correos
 * electronicos, como correos de confirmacion de registro para
 * la activacion de la cuenta de un usuario y correos para la
 * recuperacion de la contraseña, por ejemplo.
 */
public class Email {

  /*
   * Estas constantes se utilizan para establecer la configuracion
   * que se necesita para el envio de un correo electronico, como
   * el servidor que se utiliza para ello, por ejemplo.
   * 
   * En este caso, se utiliza el servidor de Gmail para el envio
   * de correos electronicos.
   */
  private static final String HOST = "smtp.gmail.com";
  private static final int PORT = 587;
  private static final String SENDER_EMAIL = "";
  private static final String EMAIL_PASSWORD = "";

  /*
   * SWCAR es la sigla de "Sistema Web para el Calculo del Agua de Riego"
   */
  private static final String SENDER_NAME = "SWCAR";

  /*
   * Los valores de la configuracion que se necesita para el envio
   * de un correo electronico, son almacenados en el objeto de
   * tipo Properties referenciado por la referencia de esta
   * constante
   */
  private static final Properties PROPERTIES = new Properties();

  /*
   * Constante utilizada para establecer el asunto de un correo electronico
   * de confirmacion de registro
   */
  private static final String CONFIRMATION_REGISTRATION = "Confirmación de registro";

  /*
   * Esta constante contiene una referencia a un objeto de tipo Authenticator
   * que se utiliza para realizar la autenticacion de una cuenta de correo
   * electronico, la cual, se utiliza para el envio de un correo electronico
   */
  private static final Authenticator AUTHENTICATOR = new Authenticator() {
    public PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(SENDER_EMAIL, EMAIL_PASSWORD);
    }
  };

  /*
   * Esta constante contiene una referencia a un objeto de tipo Session
   * que representa una sesion de correo electronico establecida
   * mediante la autenticacion de una cuenta de correo electronico y
   * unas propiedades (configuracion). Se necesita una sesion de correo
   * electronico para el envio de un correo electronico.
   */
  private static final Session SESSION = Session.getInstance(getProperties(), AUTHENTICATOR);

  /*
   * El metodo constructor tiene el modificador de acceso 'private'
   * para que ningun programador trate de instanciar esta clase
   * desde afuera, ya que todos los metodos publicos de la misma
   * son estaticos, con lo cual, no se requiere una instancia de
   * esta clase para invocar a sus metodos publicos
   */
  private Email() {

  }

  /**
   * Retorna una coleccion con la configuracion que se necesita para
   * el envio de un correo electronico, como el servidor que se
   * utiliza para ello, por ejemplo
   * 
   * @return referencia a un objeto de tipo Properties que
   * contiene la configuracion que se necesita para el envio
   * de un correo electronico, como el servidor que se utiliza
   * para ello, por ejemplo
   */
  private static Properties getProperties() {

    /*
     * Si los valores de la configuracion necesaria para el envio
     * de un correo electronico, no estan establecidos en el objeto
     * referenciado por la referencia de PROPERTIES, se los establece
     */
    if (PROPERTIES.isEmpty()) {
      PROPERTIES.put("mail.smtp.host", HOST);
      PROPERTIES.put("mail.smtp.port", PORT);
      PROPERTIES.put("mail.smtp.auth", "true");
      PROPERTIES.put("mail.smtp.starttls.enable", "true");
    }

    return PROPERTIES;
  }

  /**
   * Retorna un correo electronico con una direccion de correo
   * electronico de destino, un asunto y un mensaje (cuerpo)
   * 
   * @param recipientEmail
   * @param subject
   * @param message
   * @return referencia a un objeto de tipo Message que representa
   * un correo electronico que contiene una direccion de correo
   * electronico de destino, un asunto y un mensaje (cuerpo)
   */
  private static Message createEmail(String recipientEmail, String subject, String message) throws MessagingException, UnsupportedEncodingException {
    Message email = new MimeMessage(SESSION);
    email.setFrom(new InternetAddress(SENDER_EMAIL, SENDER_NAME));
    email.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
    email.setSubject(subject);
    email.setSentDate(new Date());
    email.setText(message);

    return email;
  }

  /**
   * Realiza el envio de un correo electronico con una direccion
   * de correo electronico de destino, un asunto y un mensaje
   * (cuerpo)
   * 
   * @param recipientEmail
   * @param subject
   * @param message
   */
  public static void sendEmail(String recipientEmail, String subject, String givenMessage) {

    try {
      Transport.send(createEmail(recipientEmail, subject, givenMessage));
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Realiza el envio de un correo electronico de confirmacion de
   * registro a la direccion de correo electronico con la que un
   * usuario se registro en la aplicacion. Este paso es para que
   * el usuario registrado active su cuenta en la aplicacion, ya
   * que de no activarla no la podra utilizar para iniciar sesion.
   * 
   * @param recipientEmail
   */
  public static void sendConfirmationEmail(String recipientEmail) {
    sendEmail(recipientEmail, CONFIRMATION_REGISTRATION, createConfirmationMessage(recipientEmail));
  }

  /**
   * @param recipientEmail
   * @return referencia a un objeto de tipo String que contiene
   * el mensaje (cuerpo) de un correo electronico de confirmacion
   * de registro
   */
  private static String createConfirmationMessage(String recipientEmail) {
    return new String("Haga clic en el siguiente enlace para confirmar su registro: http://localhost:8080/employeeService_ch3/rest/activateAccount?email="
    + recipientEmail
    + "\n\nSi no confirma el registro de su cuenta, la misma no se activará. En consecuencia, no la podrá utilizar para iniciar sesión en la aplicación.");
  }

}
