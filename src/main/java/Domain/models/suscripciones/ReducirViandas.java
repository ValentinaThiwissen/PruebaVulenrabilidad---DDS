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
import org.apache.commons.io.input.ObservableInputStream;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@DiscriminatorValue("reducirViandas")
public class ReducirViandas extends  IObservable{
    @GeneratedValue
    @Id
    private Long id;

    @Column(name ="viandaParaQueEsteLlena")
    private Integer viandasParaQueEsteLlena;

    public ReducirViandas(){
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
    public void notificar() {
        suscripcion.buscarHeladeraMasVacia();
        this.observers.forEach(o->o.registarDistribucion(suscripcion));
        Notificador notificador = Notificador.obtenerInstancia();
        String mensaje = "Faltan " + viandasParaQueEsteLlena + " viandas para que la heladera esté llena y no se puedan ingresar más viandas. ";

        this.observers.forEach(observer->notificador.
                enviarNotificacion(new Notificacion(observer, mensaje)
                        ,observer.getMedioDeNotificacionDeseado().getTipo()));

    }
    public void tieneDesperfeto(){

    }

    @Override
    public void verificarCantidadViandas(Integer cantidadViandas) {
        if(cantidadViandas >= viandasParaQueEsteLlena){
            this.notificar();
        }
    }
}


