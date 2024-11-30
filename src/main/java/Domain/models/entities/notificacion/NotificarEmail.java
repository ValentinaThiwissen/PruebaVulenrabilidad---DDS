package Domain.models.entities.notificacion;
import Domain.models.entities.config.Config;
import Domain.models.entities.notificacion.Mail;
import lombok.Getter;
import lombok.Setter;

import java.util.Properties;
import java.util.Set;
import javax.mail.*;
import javax.mail.internet.*;

public class NotificarEmail implements AdapterJavaxEmail{

    private Properties props;
    final String username = "disenopruebas2024@gmail.com";
    final String password = "gzul sdgj cwyo dtsg";

    public NotificarEmail() {
        // Configura las propiedades del envío
        props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
    }

    public void enviarMail(Notificacion notificacion) {
        Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        mailSession.setDebug(true);  // Para habilitar los mensajes de depuración

        try {
            MimeMessage mensaje = new MimeMessage(mailSession);
            mensaje.setFrom(new InternetAddress(username));
            mensaje.setSubject(notificacion.getAsunto());
            mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(notificacion.getDestinatario().getMail()));
            mensaje.setText(notificacion.getMensaje());

            // Intentar enviar el mensaje
            Transport.send(mensaje);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public void enviarMailTecnico(String mensajeArmado, String email) {
        Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        mailSession.setDebug(true);  // Para habilitar los mensajes de depuración

        try {
            MimeMessage mensaje = new MimeMessage(mailSession);
            mensaje.setFrom(new InternetAddress(username));
            mensaje.setSubject("Solicitud de trabajo");
            mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mensaje.setText(mensajeArmado);

            // Intentar enviar el mensaje
            Transport.send(mensaje);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public void enviarMailsMasivos(Set<Notificacion> notificaciones){
        notificaciones.forEach(notificaion -> enviarMail(notificaion));
    }
}
