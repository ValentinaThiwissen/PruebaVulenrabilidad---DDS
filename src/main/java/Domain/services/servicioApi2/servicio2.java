package Domain.services.servicioApi2;

import Domain.DTO.ColaboradorSolicitadoDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class servicio2 implements AdapterServicio2 {

    private HttpClient client = HttpClient.newHttpClient();

    public List<ColaboradorSolicitadoDTO> consumirApi(Integer minPuntos, Integer minDonacionUltMes, Integer cantidadMaxADevolver) throws URISyntaxException, IOException, InterruptedException {
        URI url = new URI("http://localhost:8080/colaboradores?minPuntos=" + minPuntos
                + "&minDonacionUltMes=" + minDonacionUltMes
                + "&cantidadMaxADevolver=" + cantidadMaxADevolver);
        ;

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int responseCode = response.statusCode();
        if (responseCode == 200) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<ColaboradorSolicitadoDTO>>() {
            }.getType();
            return gson.fromJson(response.body(), listType);

        } else {
            throw new RuntimeException("Error al conectarnos a la API: " + responseCode);
        }
    }
}
