package Domain.services.servicioApi2;

import Domain.DTO.ColaboradorSolicitadoDTO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface AdapterServicio2{
    public List<ColaboradorSolicitadoDTO> consumirApi(Integer minPuntos, Integer minDonacionUltMes, Integer cantidadMaxADevolver) throws URISyntaxException, IOException, InterruptedException;
}
