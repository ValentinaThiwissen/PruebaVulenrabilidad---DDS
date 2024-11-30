package Domain.services.servicioApi2;


import Domain.DTO.ColaboradorSolicitadoDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Setter
@Getter
public class buscadorDeColaboradores {
    public static AdapterServicio2 adapter;
    public static buscadorDeColaboradores instancia;

    public static buscadorDeColaboradores obtenerInstancia() { // Singleton
        if (instancia == null){
            instancia = new buscadorDeColaboradores();
            adapter = new servicio2();
        }
        return instancia;
    }

    public static List<ColaboradorSolicitadoDTO> buscarColaboradores(Integer minPuntos, Integer minDonacionUltMes, Integer cantidadMaxADevolver) throws URISyntaxException, IOException, InterruptedException {
        return adapter.consumirApi(minPuntos, minDonacionUltMes, cantidadMaxADevolver);
    }
}
