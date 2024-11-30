package Domain.models.entities.colaborador;

import Domain.models.entities.Ubicacion.Direccion;
import Domain.models.entities.personaVulnerable.PersonaVulnerable;
import Domain.models.entities.personaVulnerable.TipoDocumento;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@Entity
@Table(name = "persona_registrar")
public class  PersonaARegistrar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombrePersonaRegistrar", columnDefinition = "varchar(255)")
    private String nombre;
    @Enumerated(EnumType.STRING)
    private   TipoDocumento tipoDocumento;

    @Column(name = "nroDocumento")
    private   Integer nroDocumento;
    @Column(name = "fechaNacimiento", columnDefinition = "DATE")
    private  LocalDate fechaNacimiento;
    @Column(name = "tienePersonasACargo")
    private   Boolean tienePersonasAcargo;
    @Column(name = "situacionDeCalle")
    private  Boolean situacionDeCalle;

    @ManyToOne
    @JoinColumn(name ="direccion_id")
    private   Direccion direccion = null;

    @ManyToMany
    @JoinTable(name = "personaARegistrar_personaVulnerable", joinColumns = @JoinColumn(name = "personaARegistrar_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "persona_vulnerable_id", referencedColumnName = "id"))
    private Set<PersonaVulnerable> menoresACargo;


    public void agregarMenorACargo(PersonaVulnerable unMenor){
        this.menoresACargo.add((unMenor));
    }

    public PersonaARegistrar (String nombre, TipoDocumento tipoDocumento, Integer nroDocumento,
                              LocalDate fechaNacimiento, Boolean tienePersonasAcargo, Boolean situacionDeCalle, Set<PersonaVulnerable> menoresACargo){

        this.nombre = nombre;
        this.tipoDocumento = tipoDocumento;
        this.nroDocumento = nroDocumento;
        this.fechaNacimiento = fechaNacimiento;
        this.tienePersonasAcargo = tienePersonasAcargo;
        this.situacionDeCalle = situacionDeCalle;
        this.menoresACargo = menoresACargo;
    }
}