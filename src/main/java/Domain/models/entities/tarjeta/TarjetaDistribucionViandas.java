package Domain.models.entities.tarjeta;

import Domain.broker.BrokerHeladera;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.MotivoMovimiento;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name= "tarjeta_distribucion_vianda")

public class TarjetaDistribucionViandas {
    @Id
    @GeneratedValue
    private Long id;
    /*
    @OneToOne
    @JoinColumn (name = "duenio_id", referencedColumnName = "id")
    private Colaborador duenio;**/

    @Column(name = "codigoId", columnDefinition = "varchar(255)")
    private String codigoId;


    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "solicitudApertura_id")
    private Set<SolicitudApertura> solicitudesAperturas;

    @OneToMany
    @JoinColumn(name = "historialSolicitudes_id")
    private Set<SolicitudApertura> historialSolicitudes;


    public TarjetaDistribucionViandas() {
        solicitudesAperturas = new HashSet<>();
        historialSolicitudes = new HashSet<>();
    }

    public String obtenerCodigo(Colaborador colaborador) {
        String numeroStr = String.valueOf(colaborador.getNroDni());

        String nombre = colaborador.getNombre();
        String apellido = colaborador.getApellido();
        String primerasTresLetras;
        if (nombre.length() >= 3) {
            primerasTresLetras = nombre.substring(0, 3);
        } else if (apellido.length() >= 3) {
            primerasTresLetras = apellido.substring(0, 3);
        } else {
            primerasTresLetras = nombre + apellido;
        }

        String primerosOchoDigitos = numeroStr.length() >= 8
                ? numeroStr.substring(0, 8)
                : numeroStr;

        return primerasTresLetras + primerosOchoDigitos;
    }



    public void registarSolcitudApertura(Heladera unaHeladera, MotivoMovimiento motivo, Integer viandasAMover) {
        SolicitudApertura nuevaSolicitud = new SolicitudApertura(unaHeladera, this, motivo, viandasAMover);
        solicitudesAperturas.add(nuevaSolicitud);
        // ACA LE TENEMOS QUE AVISAR AL BROKER (SOMOS PUBLICADOR) QUE LE MANDE A LA HELADERA
        //algo.conectarConBroker(nuevaSolicitud);
        unaHeladera.agregarSolicitud(nuevaSolicitud);
        this.publicarSolicitud(nuevaSolicitud);
    }

    public void publicarSolicitud(SolicitudApertura nuevaSolicitud) {
        Gson gson = new Gson();
        String solicitudParseada = gson.toJson(nuevaSolicitud); //lo pasamos a json
        BrokerHeladera broker = nuevaSolicitud.getHeladeraUtilizada().getBroker();
        broker.publish("SOLICITUD_APERTURA", solicitudParseada);
    }

    public List<SolicitudApertura> buscarSolicitudPara(Heladera heladera) {
        return (List<SolicitudApertura>) solicitudesAperturas.stream().filter(solicitud -> solicitud.getHeladeraUtilizada() == heladera);
    }
}