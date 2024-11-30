package Domain.models.suscripciones;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.formasDeColaborar.MotivoDistribucion;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.notificacion.Notificacion;
import Domain.models.entities.notificacion.Notificador;
import Domain.models.entities.vianda.Vianda;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@DiscriminatorValue("reubicarViandas")
public class ReubicarViandas extends IObservable{
    @Id
    @GeneratedValue
    private Long id;
    

    public Sugerencia sugerirHeladeras(Set<Heladera> heladeras){
        return null;
    }

    public ReubicarViandas(){
        this.observers = new ArrayList<Colaborador>();
    }

    @Override
    public void agregarObserver(Colaborador colaborador) {
        this.observers.add(colaborador);
    }

    @Override
    public void eliminar(Colaborador colaborador) {
        this.observers.remove(colaborador);

    }

    @Override
    public void tieneDesperfeto(){
        this.notificar();
    }


    @Override
    public void notificar() {
        suscripcion.buscarHeladerasMasCercanas();
        this.observers.forEach(o->o.registarDistribucion(suscripcion));
        Notificador notificador = Notificador.obtenerInstancia();
        String mensaje = "La heladera sufrió un desperfecto y las viandas deben ser llevadas a otras heladeras a la brevedad para que las mismas no se echen a perder.";

        this.observers.forEach(o->notificador.
                enviarNotificacion(new Notificacion(o, mensaje)
                        ,o.getMedioDeNotificacionDeseado().getTipo()));

    }

    @Override
    public void verificarCantidadViandas(Integer cantidadViandas){

    }



}
