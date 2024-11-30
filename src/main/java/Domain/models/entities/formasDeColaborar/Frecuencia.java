package Domain.models.entities.formasDeColaborar;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class Frecuencia {
    private  Integer repeticion;
    private  Periodo periodo;
}
