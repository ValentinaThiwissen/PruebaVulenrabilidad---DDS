package Domain.services.recomendacionPuntosApi.entities;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface RecomendacionPuntosServices {


    @GET("recomendacion")
    Call <List<DatosGeograficos>>recomendacion(@Query("longitud") double longitud,
                                               @Query("latitud") double latitud,
                                               @Query("radio") double radio);




}
