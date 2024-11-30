package Domain.models.suscripciones;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.colaborador.MedioDeContacto;
import Domain.models.entities.formasDeColaborar.MotivoDistribucion;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.notificacion.Notificacion;
import Domain.models.entities.notificacion.Notificador;
import Domain.models.entities.notificacion.NotificarEmail;
import Domain.models.entities.vianda.Vianda;
import javax.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
@Entity
@DiscriminatorValue("agregarViandas")
public class AgregarViandas extends IObservable{
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Integer viandasDisponibles;
    //UNICO

    public AgregarViandas(){
        this.observers = new ArrayList<Colaborador>();
    }

    public AgregarViandas( Suscripcion suscripcion, Integer viandasDisponibles) {
        this.suscripcion = suscripcion;
        this.viandasDisponibles = viandasDisponibles;
        this.observers = new ArrayList<Colaborador>();;
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

    }

    public void agregarObservador(Colaborador colaborador){
        observers.add(colaborador);
    }


    @Override
    public void notificar() {
        suscripcion.buscarHeladeraMasLlena();
        this.observers.forEach(o->o.registarDistribucion(suscripcion));
        NotificarEmail notificador = new NotificarEmail();
        String mensaje = "Quedan únicamente " + viandasDisponibles + " en la heladera: " + suscripcion.getHeladerasDestino();

        this.observers.forEach(o->notificador.enviarMailTecnico(mensaje ,o.getMail()));
    }

    @Override
    public void verificarCantidadViandas(Integer cantidadViandas){
        if(cantidadViandas <= viandasDisponibles){
            this.notificar();
        }
    }
}