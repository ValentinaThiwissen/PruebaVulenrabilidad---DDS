package Domain.models.entities.formasDeColaborar;

import Domain.models.entities.MigradorDeDonantes.Fecha;
import javax.persistence.*;

import Domain.models.entities.colaborador.MedioDeContacto;
import Domain.models.entities.colaborador.MedioDePago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
@Table(name = "donacion_monetaria")
@NoArgsConstructor
@AllArgsConstructor
public class DonacionMonetaria extends FormaDeColaboracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monto")
    private Integer monto;

    @Embedded
    private Frecuencia frecuencia;

    @Column(name = "multiplicadorPesos")
    private Double multiplicadorPesos = 0.5;

    @Column(name = "fecha", columnDefinition = "DATE")
    private LocalDate fecha;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name ="medioDePago")
    private MedioDePago medioDePago;

    public DonacionMonetaria(Integer cantidad, String fechaDonacion){
        monto = cantidad;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        fecha = LocalDate.parse(fechaDonacion, formatter);
    }

    @Override
    public Double calcularPuntos() {
        return monto * multiplicadorPesos;
    }

    @Override
    public Integer cantidadViandasDonadas(){
        return 0;
    }
}
