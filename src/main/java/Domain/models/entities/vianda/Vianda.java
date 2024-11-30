package Domain.models.entities.vianda;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.Heladera;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
@Data
@Entity
@Table(name ="vianda")
@Setter
@Getter
public class Vianda {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;//PK

    @Embedded
    private Comida unaComida;

    @Column(name = "peso")
    private Integer peso;

    @ManyToOne
    @JoinColumn(name = "colaborador_id")
    private Colaborador unColaborador;

    @Column(name = "entregado")
    private Boolean entregado;

    public Vianda(){
        unaComida = new Comida();
        peso = 1;
        unColaborador = null;
        entregado = true;
    }

    public Vianda(String nombre){
        this.unaComida = new Comida(nombre);
        this.peso = this.unaComida.getCalorias();
        this.unColaborador = null;
        this.entregado = true;
    }

}


