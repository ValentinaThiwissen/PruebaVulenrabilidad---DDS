package Domain.models.entities.Ubicacion;

import Domain.models.entities.Ubicacion.PuntoGeografico;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Geodecodificacion {

    private static final double RADIO_TIERRA = 6371.0;

    private Geodecodificacion() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    public static PuntoGeografico obtenerCoordenadas(String calle, String altura, String localidad, String provincia) {
        PuntoGeografico puntoGeografico = new PuntoGeografico();

        try {
            String direccionCompleta = calle + " " + altura + ", " + localidad + ", " + provincia;
            String direccionCodificada = URLEncoder.encode(direccionCompleta, "UTF-8");
            String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + direccionCodificada;


            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(url);
                HttpResponse response = client.execute(request);
                String jsonResponse = EntityUtils.toString(response.getEntity());


                JSONArray resultados = new JSONArray(jsonResponse);


                if (resultados.length() > 0) {

                    JSONObject ubicacion = resultados.getJSONObject(0);
                    double latitud = ubicacion.getDouble("lat");
                    double longitud = ubicacion.getDouble("lon");


                    puntoGeografico.setLatitud(latitud);
                    puntoGeografico.setLongitud(longitud);
                } else {
                    System.out.println("No se pudo encontrar la ubicación.");
                }
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error al codificar la dirección: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error en la solicitud HTTP: " + e.getMessage());
        }

        return puntoGeografico;
    }

    public static double calcularDistancia(PuntoGeografico punto1, PuntoGeografico punto2) {
        double lat1 = punto1.getLatitud();
        double lon1 = punto1.getLongitud();
        double lat2 = punto2.getLatitud();
        double lon2 = punto2.getLongitud();

        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);


        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;


        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));


        return RADIO_TIERRA * c;
    }

    public static boolean estaDentroDelRadio(PuntoGeografico puntoTecnico, PuntoGeografico puntoHeladera, double radio) {
        double distancia = calcularDistancia(puntoTecnico, puntoHeladera);
        return distancia <= radio;
    }
}


