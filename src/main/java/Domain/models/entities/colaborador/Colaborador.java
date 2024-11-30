package Domain.models.entities.colaborador;
import Domain.models.entities.Ubicacion.Direccion;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.formasDeColaborar.*;
import Domain.models.entities.formulario.FormularioRespondido;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.Incidente;
import Domain.models.entities.heladera.MotivoMovimiento;
import Domain.models.entities.personaVulnerable.PersonaVulnerable;
import Domain.models.entities.personaVulnerable.TipoDocumento;
import Domain.models.entities.tarjeta.TarjetaDeHeladeras;
import Domain.models.entities.tarjeta.TarjetaDistribucionViandas;
import Domain.models.suscripciones.Suscripcion;
import lombok.Data;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name ="colaborador")
@Data
public class Colaborador {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;//PK


    @OneToMany
    @JoinColumn (name= "empresaOferente_id")//pk
    private Set<Oferta> ofertasEmpresa;

    @OneToMany
    @JoinColumn (name= "colaboradorCanje_id")
    private Set<Canje> canjesRealizados;

    // enums
    @Enumerated(EnumType.STRING)
    private TipoColaborador tipoColaborador;

    @Enumerated(EnumType.STRING)
    private TipoJuridico tipoJuridico;

    @Column(columnDefinition = "varchar(255)")
    private String razonSocial;

    @Column(name = "nombre",columnDefinition = "TEXT",nullable = false)
    private String nombre;

    @Column(name = "apellido", columnDefinition ="TEXT")
    private String apellido;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name ="direccion_id")
    private Direccion unaDireccion;

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDni;

    @Column(name ="nroDni")
    private Integer nroDni;

    @Column(name = "fechaNacimiento", columnDefinition = "DATE")
    private LocalDate nacimiento;

    @Column(name = "mailColaborador", columnDefinition ="TEXT")
    private String mail;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn (name= "colaborador_contacto_id")
    private Set<MedioDeContacto> medios;

    @OneToOne
    @JoinColumn (name = "formularioRespondido", referencedColumnName = "id")
    private FormularioRespondido formulario;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn (name= "colaboradorQueRegistro_id")
    private Set<TarjetaDeHeladeras> tarjetas = null;

    @Column(name = "puntos")
    private Double puntos;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn (name= "colaboradorHeladerasActivas_id")
    private Set<Heladera> heladerasActivas;

    @Enumerated(EnumType.STRING)
    @ElementCollection()
    @CollectionTable(name = "colaborador_formasDisponiblesDeColaborar", joinColumns = @JoinColumn(name = "colaborador_id",referencedColumnName="id"))
    @Column(name = "formasDisponiblesDeColaborar")
    private Set<TiposDeColaboracion>  formasDisponiblesDeColaborar;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn (name= "formadecolaboracion_id")
    private List<FormaDeColaboracion> colaboraciones = new ArrayList<>();

    @Column(name = "cantidadTarjetasEntregadas")
    private Integer cantidadTarjetasEntregadas = 0;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "tarjeta_id", referencedColumnName = "id")

    private TarjetaDistribucionViandas tarjetaDistribucionViandas = null;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "colaborador_heladera", joinColumns = @JoinColumn(name = "colaborador_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "heladeraSuscripta_id", referencedColumnName = "id"))
    private Set <Heladera>heladerasSuscriptas;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private MedioDeContacto medioDeNotificacionDeseado;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Usuario usuario;

    public Colaborador(){};
    public Colaborador(String[] datos, FormaDeColaboracion forma) {
        tipoDni = encontrarTipoDocumento(datos[0]);
        nroDni = Integer.parseInt(datos[1]);
        nombre = datos[2];
        apellido = datos[3];
        mail = datos[4];
        colaboraciones.add(forma);
    }
    public Colaborador(List<FormaDeColaboracion> formaDeColaborar, String nombreImpuesto) {
        formaDeColaborar = formaDeColaborar;
        nombre = nombreImpuesto;
    }

    public static Integer sum(List<FormaDeColaboracion> colaboraciones) {
        Integer sum = 0;
        for (FormaDeColaboracion colaboracion : colaboraciones) {
            sum += colaboracion.cantidadViandasDonadas();
        }
        return sum;
    }

    public void sumarPuntos(Double puntos){
        this.puntos = this.getPuntos() + puntos;
    }


    public Integer historialViandasDonadas(){
        return sum(colaboraciones);
    }


    public Integer tarjetasRepartidas() {
        return Math.toIntExact((this.tarjetas.stream().filter(TarjetaDeHeladeras::getEntregada).count())) + this.tarjetasEntregadasAntesDeMigracion();
    }

    private int tarjetasEntregadasAntesDeMigracion() {
        List<RegistroPersonaVulnerable> registros = colaboraciones.stream().
                filter(forma -> forma instanceof RegistroPersonaVulnerable).
                map(forma -> (RegistroPersonaVulnerable) forma).
                toList();
        Integer sumaCantidad = registros.stream().mapToInt(RegistroPersonaVulnerable::getCantidad).sum();
        return sumaCantidad;
    }
    public void agregarFormaDeColaborar(FormaDeColaboracion forma) {
        colaboraciones.add(forma);
    }
    public TipoDocumento encontrarTipoDocumento(String tipoDocumento) {
        return switch (tipoDocumento) {
            case "LC" -> TipoDocumento.LIBRETACIVICA;
            case "DNI" -> TipoDocumento.DNI;
            case "LE" -> TipoDocumento.LIBRETAENROLAMIENTO;
            default -> null;
        };
    }
    public void agregarColaboracion(FormaDeColaboracion formaAAgregar) {
        colaboraciones.add(formaAAgregar);
    }

    public TarjetaDeHeladeras entregarTarjeta(PersonaVulnerable personaVulnerable) {
        TarjetaDeHeladeras tarjetaAEntregar = tarjetas.stream()
                .filter(TarjetaDeHeladeras::estaDisponible)
                .findFirst()
                .orElse(null);

        if (tarjetaAEntregar != null) {
            tarjetaAEntregar.meEntregaron(personaVulnerable);

        }
        return tarjetaAEntregar;
    }

    public void intercambiarPuntos(Oferta oferta) {
        if (oferta.getPuntosNecesarios() <= puntos) {
            puntos -= oferta.getPuntosNecesarios();
        } else throw new RuntimeException("No alcanzaron los puntos");
    }
    public void agregarOferta(Oferta ofertaAAgregar){
        ofertasEmpresa.add(ofertaAAgregar);
    }
    public void reportarIncidente(Incidente incidente){
        //incidente.getHeladeraIncidente().agregarIncidente(incidente);
        incidente.reportarTecnicosMasCercano(incidente.getHeladeraIncidente());

    }

    public void generarSolicutudMovimientoViandas(Heladera heladera, MotivoMovimiento motivo, Integer viandasAMover){
        tarjetaDistribucionViandas.registarSolcitudApertura(heladera, motivo, viandasAMover);
    }

    public void registarDistribucion(Suscripcion suscripcion) {
        //recibir_notificacion()
        generarSolicutudMovimientoViandas(suscripcion.getHeladeraOrigen(), MotivoMovimiento.RETIRARVIANDA, suscripcion.getViandasADistribuir());
        Set<Heladera> heladerasDestino = suscripcion.getHeladerasDestino();
        heladerasDestino.forEach(heladera -> generarSolicutudMovimientoViandas(heladera, MotivoMovimiento.INGRESARVIANDA, suscripcion.getViandasADistribuir()));
        DistribucionViandas distribucion = new DistribucionViandas(suscripcion, tarjetaDistribucionViandas);
        this.agregarColaboracion(distribucion);
    }


    public void sumarTarjetaEntregada() {
        if(this.cantidadTarjetasEntregadas == null){
            this.cantidadTarjetasEntregadas = 1;
        }else{
            this.cantidadTarjetasEntregadas  = +1;
        }

    }
}