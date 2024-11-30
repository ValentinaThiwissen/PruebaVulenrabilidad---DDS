package Domain.models.suscripciones;

import Domain.models.entities.heladera.Heladera;
import lombok.Data;

import java.util.Set;
@Data
public class Sugerencia {
    private Set<Heladera> heladerasSugeridas;

}
