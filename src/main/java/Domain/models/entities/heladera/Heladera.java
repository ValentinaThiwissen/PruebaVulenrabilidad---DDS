package Domain.models.entities.heladera;

import Domain.broker.BrokerHeladera;
import Domain.models.entities.Ubicacion.Direccion;
import Domain.models.entities.Ubicacion.Geodecodificacion;
import Domain.models.entities.Ubicacion.PuntoGeografico;
import Domain.models.entities.tarjeta.SolicitudApertura;
import Domain.models.entities.tarjeta.TarjetaDistribucionViandas;
import Domain.models.entities.tecnico.Tecnico;
import Domain.models.entities.tecnico.VisitaTecnica;
import Domain.models.entities.vianda.Vianda;
import Domain.models.suscripciones.IObservable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.*;

@Data
@Entity
@Table(
        name = "heladera"
)

public class Heladera {

    @Transient
    private ReceptorMovimiento receptorMovimiento;
    @Transient
    private ReceptorTemperatura receptorTemperatura;
    @Transient
    private ReceptorSolicitud receptorSolicitud;

    @Column(name = "ultimaPublicacionTemperatura", columnDefinition = "TIMESTAMP")
    private LocalDateTime ultimaPublicacionTemperatura;

    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Id
    public Long id;
    @Column(
            name = "nombreHeladera",
            columnDefinition = "TEXT"
    )
    private String nombreHeladera;

    @Embedded
    public PuntoGeografico unPuntoGeografico;
    @Column(
            name = "capacidad"
    )
    private Integer capacidad;
    @Column(
            name = "fechaActivacion",
            columnDefinition = "DATE"
    )
    private LocalDate fechaActivacion;
    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "modelo_heladera_id"
    )
    private Modelo unModelo;
    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "heladera_id"
    )
    private Set<Vianda> viandasOcupadas = new HashSet();
    @OneToMany
    @JoinColumn(
            name = "heladeraSolicitada_id"
    )
    private Set<SolicitudApertura> solicitudesDeApertura = new HashSet();
    @OneToMany
    @JoinColumn(
            name = "historialMovimientos_id"
    )
    private Set<MovimientoVianda> historialMovimientos = new HashSet();
    @Column(
            name = "tiempoPermitido"
    )
    private Integer tiempoPermitidoAperturaMinutos;

    @Column(name = "activa")
    private Boolean estaActiva;

    @OneToMany
    @JoinColumn(
            name = "heladeraFallada_id"
    )
    private Set<VisitaTecnica> historialVisitasTecnicas = new HashSet();
    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "heladera_casosSuscripcion",
            joinColumns = {@JoinColumn(
                    name = "heladera_id",
                    referencedColumnName = "id"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "casosSuscripcion_id",
                    referencedColumnName = "id"
            )}
    )
    private Set<IObservable> casosSuscripcion;
    @Setter
    @Transient
    private BrokerHeladera broker;

    public Heladera(LocalDate fechaDeActivacion) {
        this.fechaActivacion = fechaDeActivacion;
    }

    /*public Heladera(String nombre, List<Incidente> incidentes) {
        this.nombreHeladera = nombre;
        this.historialIncidentes = incidentes;
    }*/
    public Heladera(String nombre) {
        this.nombreHeladera = nombre;
    }
    public Heladera() {
    }


    public void agregarViandas(Vianda... viandas) {
        Collections.addAll(this.viandasOcupadas, viandas);
    }

    public void ingresarVianda(Vianda unaVianda) throws HeladeraCapacidadExcedidaException {
        if (this.cantidadViandasOcupadas() < this.capacidad) {
            this.viandasOcupadas.add(unaVianda);
        } else {
            throw new HeladeraCapacidadExcedidaException("La capacidad de la heladera ha sido excedida. No se puede ingresar más viandas.");
        }
    }

    public Integer cantidadViandasOcupadas() {
        return this.viandasOcupadas.size();
    }

    public Integer mesesActiva() {
        return this.estaActiva ? Period.between(LocalDate.now(), this.fechaActivacion).getMonths() * -1 : 0;
    }

    public void agregarSolicitud(SolicitudApertura nuevaSolicitud) {
        this.solicitudesDeApertura.add(nuevaSolicitud);
    }

    public void intentoApertura(SolicitudApertura solicitudApertura) {
        MovimientoVianda nuevoMovimiento = new MovimientoVianda();
        nuevoMovimiento.setTarjeta(solicitudApertura.getTarjetaSolicitante());
        if (this.puedeAbrirHeladera(nuevoMovimiento)) {
            nuevoMovimiento.setMotivoMovimiento(solicitudApertura.getMotivo());
            nuevoMovimiento.setViandas(solicitudApertura.getViandas());
            nuevoMovimiento.setIntentoAperturaSatisfactorio(true);
            this.solicitudesDeApertura.remove(solicitudApertura);
            this.historialMovimientos.add(nuevoMovimiento);
        } else {
            nuevoMovimiento.setIntentoAperturaSatisfactorio(false);
            this.historialMovimientos.add(nuevoMovimiento);
            throw new RuntimeException("No tiene permitido abrir heladera: " + this.nombreHeladera);
        }
    }

    public Boolean puedeAbrirHeladera(MovimientoVianda movimientoVianda) {
        SolicitudApertura solicitudHecha = this.buscarSolicitudDe(movimientoVianda);
        if (solicitudHecha == null) {
            throw new RuntimeException("No tiene una solicitud para abrir la heladera: " + this.nombreHeladera);
        } else {
            return this.verificarTiempoApertura(movimientoVianda, solicitudHecha);
        }
    }

    public Boolean verificarTiempoApertura(MovimientoVianda movimientoVianda, SolicitudApertura solicitudHecha) {
        return solicitudHecha.laSolicitudEsValida(movimientoVianda.getHoraMovimiento());
    }

    public SolicitudApertura buscarSolicitudDe(MovimientoVianda movimientoVianda) {
        return (SolicitudApertura)this.solicitudesDeApertura.stream().filter((solicitud) -> {
            return solicitud.getTarjetaSolicitante().getCodigoId().equals(movimientoVianda.getTarjeta().getCodigoId());
        }).findFirst().orElse(null);
    }


    public void aumentarStock(Set<Vianda> viandas) {
        if (this.viandasOcupadas.size() + 1 <= this.capacidad) {
            this.viandasOcupadas.addAll(viandas);
            this.chequearSuscripciones();
        }

    }

    private void chequearSuscripciones() {
        this.casosSuscripcion.forEach((suscripcion) -> {
            suscripcion.verificarCantidadViandas(this.viandasOcupadas.size());
        });
    }

    public void disminuirStock(Set<Vianda> viandas) {
        if (this.capacidad - 1 >= 0) {
            this.viandasOcupadas.removeAll(viandas);
        }

        this.chequearSuscripciones();
    }

    public Set<MovimientoVianda> movimientosExitososViandas() {
        Set<MovimientoVianda> movimientosExitosos = (Set)this.getHistorialMovimientos().stream().filter(MovimientoVianda::getIntentoAperturaSatisfactorio).collect(Collectors.toSet());
        return movimientosExitosos;
    }

    public void agregarVisitaTecnica(VisitaTecnica unaVisita) {
        this.historialVisitasTecnicas.add(unaVisita);
    }

    public Direccion getUbicacionHeladera() {
        return this.buscarDireccion(this.unPuntoGeografico);
    }

    public Direccion buscarDireccion(PuntoGeografico punto) {
        return null;
    }


    public Set<Vianda> viandasParaLaDistribucion(Integer viandasADistribuir) {
        int[] contador = new int[]{0};
        Set<Vianda> viandas = (Set)this.viandasOcupadas.stream().filter((vianda) -> {
            int var10003 = contador[0];
            int var10000 = contador[0];
            contador[0] = var10003 + 1;
            return var10000 < viandasADistribuir;
        }).collect(Collectors.toSet());
        this.disminuirStock(viandas);
        return viandas;
    }

    public Set<Vianda> abrirHeladeraYSacarViandas(Integer viandasMovidas, TarjetaDistribucionViandas tarjeta) {
        MovimientoVianda movimiento = new MovimientoVianda(tarjeta, MotivoMovimiento.RETIRARVIANDA, viandasMovidas);
        return this.puedeAbrirHeladera(movimiento) ? this.viandasParaLaDistribucion(viandasMovidas) : null;
    }

    public void abrirHeladeraYAgregarViandas(Set<Vianda> viandasAMover, TarjetaDistribucionViandas tarjeta) {
        MovimientoVianda movimiento = new MovimientoVianda(tarjeta, MotivoMovimiento.INGRESARVIANDA, viandasAMover.size());
        if (this.puedeAbrirHeladera(movimiento)) {
            this.aumentarStock(viandasAMover);
        }

    }

    public Integer capacidadDisponible() {
        return this.capacidad - this.cantidadViandasOcupadas();
    }

    public Boolean pasaronMasDe5Mins(){
        if(this.ultimaPublicacionTemperatura == null ){
            this.ultimaPublicacionTemperatura = LocalDateTime.now();
        }
        LocalDateTime horaActual = LocalDateTime.now();
        return ultimaPublicacionTemperatura.isAfter(horaActual.minusMinutes(5));
    }

    public Tecnico buscarElTecnicoMasCercano(Set<Tecnico> tecnicos) {
        if (tecnicos == null || tecnicos.isEmpty()) {
            throw new IllegalArgumentException("La lista de técnicos está vacía o es nula.");
        }

        Tecnico tecnicoMasCercano = null;
        double distanciaMinima = Double.MAX_VALUE;

        for (Tecnico tecnico : tecnicos) {
            double distancia = Geodecodificacion.calcularDistancia(tecnico.getAreaDeCobertura().getPuntoGeografico(), this.unPuntoGeografico);

            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                tecnicoMasCercano = tecnico;
            }
        }

        return tecnicoMasCercano;
    }
}
