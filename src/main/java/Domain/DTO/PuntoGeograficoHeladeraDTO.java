package Domain.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.ws.rs.GET;

@Getter
@Setter
@NoArgsConstructor
public class PuntoGeograficoHeladeraDTO {
    private String nombreHeladera;
    private Double latitud;
    private Double longitud;

    public PuntoGeograficoHeladeraDTO(String nombreHeladera, Double latitud, Double longitud) {
        this.nombreHeladera = nombreHeladera;
        this.latitud = latitud;
        this.longitud = longitud;
    }
}
