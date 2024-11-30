package Domain.models.suscripciones;

import Domain.models.entities.colaborador.Colaborador;
//import jakarta.persistence.*;
import javax.persistence.*;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name ="casos_a_suscribir")
@DiscriminatorColumn(name = "casosSuscripcion")
public abstract class  IObservable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn (name = "suscripcion_id", referencedColumnName = "id")
    public  Suscripcion suscripcion;

    @OneToMany
    @JoinColumn(name = "casoSuscripcion_id" )
    protected List<Colaborador> observers = new ArrayList<>();
    public abstract void agregarObserver(Colaborador colaborador);
    public abstract void eliminar(Colaborador colaborador);
    public abstract void notificar();
    
    public abstract void verificarCantidadViandas(Integer cantidadViandas);

    public abstract void tieneDesperfeto();

}
