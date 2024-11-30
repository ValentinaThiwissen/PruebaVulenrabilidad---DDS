package Domain.models.entities.heladera;

import Domain.models.entities.tarjeta.TarjetaDistribucionViandas;
import Domain.models.entities.vianda.Vianda;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "movimiento_vianda")
public class MovimientoVianda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(name = "fechaMovimiento", columnDefinition = "DATE")
    private LocalDate fechaMovimiento;

    @Column(name = "horaMovimiento", columnDefinition = "TIMESTAMP")
    private LocalDateTime horaMovimiento;

    @OneToOne
    @JoinColumn(name = "tarjeta_id", referencedColumnName = "id")
    private TarjetaDistribucionViandas tarjeta;

    @Enumerated(EnumType.STRING)
    private MotivoMovimiento motivoMovimiento;

    @Column(name= "cantidadViandas")
    private Integer viandas;

    @Column(name = "intentoAperturaSatisfactorio")
    private Boolean intentoAperturaSatisfactorio;

    /*Boolean puedeAbrir(){
        // si tarjeta es null devuelve false
        // si tiene tarjeta pero paso mas tiempo del permitido devuelve false
        // si el tiempo permitido esta bien devuelve true
    }*/

    public MovimientoVianda(TarjetaDistribucionViandas tarjeta, MotivoMovimiento motivoMovimiento, Integer viandasMovidas) {
        this.fechaMovimiento = LocalDate.now();
        this.horaMovimiento = LocalDateTime.now();
        this.tarjeta = tarjeta;
        this.motivoMovimiento = motivoMovimiento;
        this.viandas = viandasMovidas;
    }
    public MovimientoVianda() {
        this.fechaMovimiento = LocalDate.now();
        this.horaMovimiento = LocalDateTime.now();
    }

}
