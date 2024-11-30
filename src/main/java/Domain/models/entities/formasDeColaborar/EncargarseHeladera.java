package Domain.models.entities.formasDeColaborar;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.Heladera;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import jdk.jfr.Enabled;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="encargarse_heladera")
public class EncargarseHeladera extends FormaDeColaboracion{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "heladeraACargo_id", referencedColumnName = "id")
    private Heladera heladeraACargo;

    @Column(name = "multiplicadorHeladeras")
    private Double multiplicadorHeladeras = 5.0;

    @Column(name = "fechaNacimiento", columnDefinition = "DATE")
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "colaboradorEncargado_id", referencedColumnName = "id")
    private Colaborador colaboradorEncargado;

    public  EncargarseHeladera(Heladera heladera, Colaborador colaborador){
        heladeraACargo = heladera;
        fecha = heladera.getFechaActivacion();
        colaboradorEncargado = colaborador;
    }

    public EncargarseHeladera() {

    }

    @Override
    public Double calcularPuntos() {
        return this.cantHeladerasActivas() * this.sumaToriaMeseaActivas() * multiplicadorHeladeras;
    }
    Integer cantHeladerasActivas(){
        return colaboradorEncargado.getHeladerasActivas().size();
    }

    Integer sumaToriaMeseaActivas() {
        return (colaboradorEncargado.getHeladerasActivas().stream().mapToInt(heladera -> heladera.mesesActiva()).sum());
    }

    @Override
    public Integer cantidadViandasDonadas(){
        return 0;
    }

}
