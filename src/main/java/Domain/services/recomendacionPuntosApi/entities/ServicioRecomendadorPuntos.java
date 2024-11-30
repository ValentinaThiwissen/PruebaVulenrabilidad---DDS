package Domain.services.recomendacionPuntosApi.entities;

import org.apache.commons.lang3.ObjectUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;
import java.util.List;

public class ServicioRecomendadorPuntos {

    private Retrofit retrofit;
    private static final String urlApi = "https://9e6e006b-8895-470a-9776-2ceefe41511b.mock.pstmn.io/api/"; // si o si la absoluta
    private static ServicioRecomendadorPuntos instancia = null;
    private ServicioRecomendadorPuntos(){
        this.retrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .addConverterFactory(GsonConverterFactory.create()).
                build();//patron build

    }

    public static ServicioRecomendadorPuntos instancia() {
        if(instancia == null){
            instancia = new ServicioRecomendadorPuntos();

        }
        return instancia;
    }
    public List<DatosGeograficos> datosGeograficos(double longitud, double latitud, double radio) throws IOException {
        RecomendacionPuntosServices recomendacionPuntosServices = this.retrofit.create(RecomendacionPuntosServices.class);

        Call<List<DatosGeograficos>> requestDatosDeColocacion = recomendacionPuntosServices.recomendacion(longitud, latitud,radio);
        Response<List<DatosGeograficos>> responsePuntosColocacion =requestDatosDeColocacion.execute();
        return responsePuntosColocacion.body();

    }
}

