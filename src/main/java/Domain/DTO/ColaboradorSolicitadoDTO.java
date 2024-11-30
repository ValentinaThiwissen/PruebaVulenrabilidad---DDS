package Domain.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ColaboradorSolicitadoDTO {
    private String nombre;
    private String apellido;
    private String email;
    private Double puntaje;
    private Integer cantidadDonaciones;
}
