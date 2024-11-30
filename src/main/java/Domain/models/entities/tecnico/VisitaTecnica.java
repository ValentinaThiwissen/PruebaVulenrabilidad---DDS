package Domain.models.entities.tecnico;

import Domain.models.entities.heladera.Heladera;
import lombok.Data;

import javax.persistence.*;
import java.nio.file.Path;
import java.time.LocalDate;
@Data
@Entity
@Table (name = "visitaTecnica")

public class VisitaTecnica {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "fechaVisita", columnDefinition = "DATE")
    private LocalDate fechaVisita;

    @Column(name = "descripcionTrabajo",columnDefinition = "TEXT")
    private  String descripcionTrabajo;

    @Column(name = "fotoTrabajo",columnDefinition = "TEXT")
    private String fotoTrabajo;

    @Column(name ="solucionadoTrabajo")
    private  Boolean solucionadoTrabajo;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "heladeraAArreglar_id")
    private Heladera heladeraAArreglar;


    public VisitaTecnica(LocalDate fechaVisita, Boolean solucionadoTrabajo) {
        this.fechaVisita = fechaVisita;
        this.solucionadoTrabajo = solucionadoTrabajo;
    }

    public VisitaTecnica() {

    }
}
