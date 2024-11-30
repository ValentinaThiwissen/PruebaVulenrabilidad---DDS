package Domain.models.entities.heladera;

import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.tarjeta.SolicitudApertura;
import Domain.models.entities.tarjeta.TarjetaDistribucionViandas;
import Domain.models.repository.TarjetaDistribucionViandaRepository;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Getter
@Setter

public class ReceptorSolicitud {
    //private  Heladera heladera;
    private TarjetaDistribucionViandaRepository tarjetaDistribucionViandaRepository ;

    public void recibirDato(String tarjeta, Heladera heladera) {
        TarjetaDistribucionViandas tarjetaSolicitud = tarjetaDistribucionViandaRepository.buscar(Long.valueOf(tarjeta));

        List<SolicitudApertura> solicitudes = tarjetaSolicitud.buscarSolicitudPara(heladera);


        SolicitudApertura ultimaSolicitud = solicitudes.get(0);
        for (SolicitudApertura solicitud : solicitudes) {
            if (solicitud.getFechaDeSolicitud().isAfter(ultimaSolicitud.getFechaDeSolicitud())) {
                ultimaSolicitud = solicitud;
            }
        }
        heladera.intentoApertura(ultimaSolicitud);
    }

}
