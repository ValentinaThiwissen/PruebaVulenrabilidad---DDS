package Domain.models.entities.tarjeta;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.MotivoMovimiento;
import Domain.models.entities.vianda.Vianda;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name ="solicitudApertura")
public class SolicitudApertura {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "fechaSolicitud", columnDefinition = "DATE")
    private LocalDate fechaDeSolicitud;

    @Column(name = "horaSolicitudTarjeta", columnDefinition = "TIMESTAMP")
    private LocalDateTime horaSolicitudTarjeta;

    @OneToOne
    @JoinColumn(name ="heladeraUtilizada_id", referencedColumnName = "id")
    private Heladera heladeraUtilizada;

    @ManyToOne
    @JoinColumn(name ="tarjetaSolicitante_id")
    private TarjetaDistribucionViandas tarjetaSolicitante;//tarjeta tiene el colaborador solicoitante

    @Enumerated(EnumType.STRING)
    private MotivoMovimiento motivo;

    @Column (name = "horasDeValidez")
    private Integer horasDeValidez = 3;

    @Column(name = "cantidadViandas")
    private Integer viandas;


    public SolicitudApertura(Heladera unaHeladera, TarjetaDistribucionViandas tarjeta, MotivoMovimiento motivoSolicitud, Integer viandasAMover) {
        fechaDeSolicitud = LocalDate.now();
        heladeraUtilizada = unaHeladera;
        horaSolicitudTarjeta = LocalDateTime.now();
        tarjetaSolicitante = tarjeta;;
        motivo = motivoSolicitud;
        viandas = viandasAMover;
    }

    public SolicitudApertura() {

    }


    public void ultimaSolicitud(){

    }

    public Boolean laSolicitudEsValida(LocalDateTime fecha) {
        return !seVencioLaSolicitud(fecha);
    }


    public Boolean seVencioLaSolicitud(LocalDateTime fecha){

        Duration diferencia = Duration.between(horaSolicitudTarjeta, fecha);

        return diferencia.compareTo(Duration.ofHours(horasDeValidez)) < 0;
    }

}
