package Domain.models.entities.tecnico;
import Domain.models.entities.Ubicacion.AreaDeCobertura;
import Domain.models.entities.Ubicacion.PuntoGeografico;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.Incidente;
import Domain.models.entities.personaVulnerable.TipoDocumento;

import Domain.models.entities.colaborador.MedioDeContacto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table (name ="tecnico")

public class Tecnico {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name= "nombre", columnDefinition = "varchar(255)",nullable = false)
    private String nombre;

    @Column(name= "apellido", columnDefinition = "varchar(255)",nullable = false)
    private String apellido;

    @Column(name= "cuil",nullable = false)
    private Long cuil;

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @Column(name= "nroDocumento",nullable = false)
    private Integer nroDocumento;

    @Column(name= "mail", columnDefinition = "varchar(255)",nullable = false)
    private String mail;

    @Embedded
    private Set<MedioDeContacto> medio;

    @Embedded
    private AreaDeCobertura areaDeCobertura;

    @OneToMany
    @JoinColumn (name= "tecnico_id")
    private Set <VisitaTecnica> visitas;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Usuario usuario;

    public Tecnico(String nombre, String apellido, Long cuil, TipoDocumento tipoDocumento, Integer nroDocumento, Set<MedioDeContacto> medio, AreaDeCobertura areaDeCobertura, Set<VisitaTecnica> visitas) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cuil = cuil;
        this.tipoDocumento = tipoDocumento;
        this.nroDocumento = nroDocumento;
        this.medio = medio;
        this.areaDeCobertura = areaDeCobertura;
        this.visitas = visitas;
    }

    public Tecnico() {


    }

    public VisitaTecnica arreglarUnaHeladera(Incidente incidente){
        Random randomMode = new Random();
        boolean solucionadoElTrabajo = randomMode.nextBoolean();
        LocalDate fechaVisita = LocalDate.now();
        VisitaTecnica unaVisita = new VisitaTecnica(fechaVisita, solucionadoElTrabajo);

        visitas.add(unaVisita);
        if(solucionadoElTrabajo){
            incidente.getHeladeraIncidente().setEstaActiva(true);
            incidente.getHeladeraIncidente().agregarVisitaTecnica(unaVisita);
            visitas.add(unaVisita);


        }else {
            incidente.getHeladeraIncidente().agregarVisitaTecnica(unaVisita);
            visitas.add(unaVisita);

        }
        return unaVisita;
    }

}
