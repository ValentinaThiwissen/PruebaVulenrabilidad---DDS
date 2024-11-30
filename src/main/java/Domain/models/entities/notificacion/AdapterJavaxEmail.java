package Domain.models.entities.notificacion;

import java.util.Set;

public interface AdapterJavaxEmail {

    public void enviarMail(Notificacion notificacion);
    public void enviarMailsMasivos(Set<Notificacion> notificaciones);

}
