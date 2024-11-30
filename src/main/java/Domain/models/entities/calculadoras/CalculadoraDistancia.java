package Domain.models.entities.calculadoras;

import Domain.models.entities.Ubicacion.Direccion;
import Domain.models.entities.Ubicacion.PuntoGeografico;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.TravelMode;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.apache.commons.math3.analysis.function.Sin;

public class CalculadoraDistancia implements  AdapterCalculadoraDistancia{
    private static int radio_tierra = 6371;
    private static CalculadoraDistancia instance = new CalculadoraDistancia();
    public static double calcularDistancia(PuntoGeografico ubicacion1, PuntoGeografico ubicacion2) {
        double lat1 = Math.toRadians(ubicacion1.getLatitud());
        double lon1 = Math.toRadians(ubicacion1.getLongitud());
        double lat2 = Math.toRadians(ubicacion2.getLatitud());
        double lon2 = Math.toRadians(ubicacion2.getLongitud());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distancia = radio_tierra * c;
        return distancia;
    }

    public static void main(String[] args) {
        PuntoGeografico ubicacion1 = new PuntoGeografico(-34.6037, -58.3816);
        PuntoGeografico ubicacion2 = new PuntoGeografico(40.7128, -74.0060);

        double distancia = calcularDistancia(ubicacion1, ubicacion2);
        System.out.printf("La distancia entre las ubicaciones es: %.2f kilómetros\n", distancia);
    }

}
