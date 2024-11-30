package Domain.models.entities.Ubicacion;

import lombok.Data;

import javax.persistence.*;

@Data
@Embeddable
public class AreaDeCobertura {
   @Embedded
    PuntoGeografico puntoGeografico;
    @Column
    Integer radioDeCobertura;

    public AreaDeCobertura() {
    }
}
