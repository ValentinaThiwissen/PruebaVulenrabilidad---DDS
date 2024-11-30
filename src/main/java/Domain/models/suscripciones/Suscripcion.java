package Domain.models.suscripciones;

import Domain.models.entities.calculadoras.CalculadoraDistancia;
import Domain.models.entities.formasDeColaborar.MotivoDistribucion;
import Domain.models.entities.heladera.Heladera;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name ="suscripcion")

public class Suscripcion {
    @Id
    @GeneratedValue
    private  Long id;

    @OneToMany
    @JoinColumn (name= "suscripcionHeladeraDestino")
    private Set<Heladera> heladerasDestino = new HashSet<>();

    @ManyToOne
    @JoinColumn(name ="heladeraOrigen_id")//->
    private Heladera heladeraOrigen;

    @Enumerated(EnumType.STRING)
    private MotivoDistribucion motivo;

    @Column
    private Integer viandasADistribuir;

    @OneToMany
    @JoinColumn (name= "suscripcionHeladerasParaViandas")
    private Set<Heladera> heladerasParaLLevarViandas = new HashSet<>();// repo

    public Suscripcion(Set<Heladera> heladerasDestino, Heladera heladeraOrigen, MotivoDistribucion motivo, Integer viandasADistribuir,Set<Heladera> heladerasParaLLevarViandas) {
        this.heladerasDestino = heladerasDestino;
        this.heladeraOrigen = heladeraOrigen;
        this.motivo = motivo;
        this.viandasADistribuir = viandasADistribuir;
        this.heladerasParaLLevarViandas = heladerasParaLLevarViandas;
    }

    public Suscripcion() {

    }

    public void buscarHeladeraMasVacia() {
        Heladera heladeraMasVacia = Collections.min(heladerasParaLLevarViandas, Comparator.comparingInt(Heladera::cantidadViandasOcupadas));
        heladerasDestino.clear();
        heladerasDestino.add(heladeraMasVacia);
    }

    public void  buscarHeladeraMasLlena() {
        Heladera heladeraMasLlena = Collections.max(heladerasParaLLevarViandas, Comparator.comparingInt(Heladera::cantidadViandasOcupadas));
        heladerasDestino.clear();
        heladerasDestino.add(heladeraMasLlena);
    }


    public void buscarHeladerasMasCercanas(){
        Set<Heladera> heladerasMasCercanas = (Set<Heladera>) heladerasParaLLevarViandas.stream().
                sorted(Comparator.comparingDouble(heladera -> CalculadoraDistancia.calcularDistancia(heladeraOrigen.getUnPuntoGeografico(),
                        heladera.getUnPuntoGeografico())));

        Integer contadorViandas = viandasADistribuir;
        heladerasDestino.clear();

        while(contadorViandas > 0){
            // encuentra la heladera mas cercana
            Heladera heladera = heladerasMasCercanas.stream().findFirst().orElse(null);
            // me fijo cuantas viandas puedo guardarCanje en esa heladera
            Integer viandasDisponibles = heladera.capacidadDisponible();
            // las guardo en la heladera?
            // le resto a las viandas que hay que distribuir las que ya distribui
            contadorViandas -= viandasDisponibles;
            // agrego al set de heladeras destino la heladera a la que distribui
            heladerasDestino.add(heladera);
            // me vuelvo a fijar con la siguiente heladera mas cercana (si todavia no distribui todas las que tengo que distribuir)
            heladerasMasCercanas.remove(heladera);

        }
    }


}
