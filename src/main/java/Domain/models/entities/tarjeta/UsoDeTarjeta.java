package Domain.models.entities.tarjeta;

import Domain.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter

@Entity
@Table (name ="usoDeTarjeta")
public class UsoDeTarjeta {
    @GeneratedValue
    @Id
    @Column(name = "fechaDeUso", columnDefinition = "TIME")
    private LocalDate fechaDeUso;

    @ManyToOne
    @JoinColumn(name ="uso_tarjeta_id")
    private Heladera heladeraUtilizada;

    public UsoDeTarjeta(Heladera unaHeladera) {
        this.fechaDeUso = LocalDate.now();
        this.heladeraUtilizada = unaHeladera;
    }
}
