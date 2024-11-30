package Domain.models.entities.heladera;

import Domain.controller.HeladeraController.ReceptorController;
import lombok.Data;

@Data
public class ReceptorMovimiento {
    /* private  Heladera heladera;*/
    private ReceptorController receptorController = new ReceptorController();

    public void recibirDato(String dato, Heladera heladera) {
        Boolean datoParseado=Boolean.parseBoolean(dato);
        this.evaluarMovimiento(datoParseado, heladera);
    }

    public void evaluarMovimiento (Boolean datoParseado, Heladera heladera) {
        if(datoParseado){
            receptorController.alarmaMovimiento(heladera);
        }
    }
}
