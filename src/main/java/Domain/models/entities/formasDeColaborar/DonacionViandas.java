package Domain.models.entities.formasDeColaborar;

import Domain.models.entities.MigradorDeDonantes.Fecha;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.MotivoMovimiento;
import Domain.models.entities.heladera.MovimientoVianda;
import Domain.models.entities.tarjeta.TarjetaDistribucionViandas;
import Domain.models.entities.vianda.Vianda;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "donacionViandas")
public class DonacionViandas extends FormaDeColaboracion{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name= "donacionVianda_id")
    private Set<Vianda> viandasDonadas;

    @Column(name = "multiplicadorDonacion")
    private Double multiplicadorDonacion = 1.5;

    @Column(name = "fecha", columnDefinition = "DATE")
    private LocalDate fecha;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name ="heladeraUtilizada_id")
    private Heladera heladeraUtilizada;

    @Column(name = "cantidadViandas")
    private Integer cantidadViandas;

    public DonacionViandas(Set<Vianda> viandas){
        viandasDonadas = viandas;
        fecha = LocalDate.now();
    }

    public DonacionViandas(Integer viandas, String fechaDonacion){
        cantidadViandas = viandas;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        fecha = LocalDate.parse(fechaDonacion, formatter);
    }

    public DonacionViandas() {

    }


    @Override
    public Double calcularPuntos() {
        return Double.valueOf(this.viandasDonadas.size() * multiplicadorDonacion);
    }

    @Override
    public Integer cantidadViandasDonadas(){
        return this.viandasDonadas.size();
    }
}
