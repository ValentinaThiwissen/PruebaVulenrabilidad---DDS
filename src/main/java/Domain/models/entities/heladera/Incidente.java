package Domain.models.entities.heladera;

import Domain.models.entities.calculadoras.BuscadorTecnicos;
import Domain.models.entities.calculadoras.CalculadoraDistancia;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.tecnico.Tecnico;
import lombok.Data;

import javax.persistence.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
@Data
@Entity
@Table(name ="incidente")
public class Incidente {
    @GeneratedValue
    @Id
    private Long id;//PK
    /*
    @Column(name = "nombre")
    private String nombre;**/

    @Transient
    private BuscadorTecnicos buscadorTecnicos;

    @Column(name = "fechaIncidente", columnDefinition = "DATE")
    private LocalDate fechaIncidente;

    @Column(name = "horaIncidente", columnDefinition = "TIMESTAMP")
    private LocalDateTime horaIncidente;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "heladeraIncidente_id")
    private Heladera heladeraIncidente;

    @Enumerated(EnumType.STRING)
    private TipoIncidente tipoIncidente;

    @OneToOne(optional = true)
    @JoinColumn(name = "colaboradorQueReporto_id", referencedColumnName = "id")
    private Colaborador colaboradorQueReporto;

    @Column(name = "descripcion", columnDefinition = "TEXT", nullable = true)
    private String descripcion;

    @Column(name = "fotoIncidente", columnDefinition = "TEXT")
    private String fotoIncidente;

    @Enumerated(EnumType.STRING)
    private TipoAlerta tipoAlerta;

    public Incidente( LocalDate fechaIncidente, LocalDateTime horaIncidente, TipoIncidente tipoIncidente, String descripcion, String fotoIncidente) {

        this.fechaIncidente = fechaIncidente;
        this.horaIncidente = horaIncidente;
        this.tipoIncidente = tipoIncidente;
        this.descripcion = descripcion;
        this.fotoIncidente = fotoIncidente;

    }

    public Incidente(String unNombre, TipoIncidente unIncidente, Heladera unaHeladera, TipoAlerta unaAlerta) {
        tipoIncidente = unIncidente;
        fechaIncidente = LocalDate.now();
        horaIncidente = LocalDateTime.now();
        heladeraIncidente = unaHeladera;
        tipoAlerta = unaAlerta;
    }


    public Incidente() {

    }

    public void reportarTecnicosMasCercano (Heladera heladeraIncidente){
        /*Set<Tecnico> tecnicos = tecnicoRepository.buscarTodos();
        Tecnico unTecnico = buscadorTecnicos.buscarTecnicoMasCercano(tecnicos, heladeraIncidente.getUnPuntoGeografico());
        unTecnico.arreglarUnaHeladera(this); (esta ok pero falta reppositorio)*/
        }

    }
