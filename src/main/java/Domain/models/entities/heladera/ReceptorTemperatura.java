package Domain.models.entities.heladera;

import Domain.controller.HeladeraController.ReceptorController;
import lombok.Data;
import lombok.Setter;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReceptorTemperatura {
    /* private Heladera heladera;*/
    private ReceptorController receptorController = new ReceptorController();

    public Boolean tieneTemperaturaAceptable(Integer  temperaturaActual, Heladera heladera){
        return (heladera.getUnModelo().getTemperaturaMinima() < temperaturaActual) && (heladera.getUnModelo().getTemperaturaMaxima() > temperaturaActual);
    }
    /* public void alertaFallaConexion(){
          heladera.alarmaTemperaturaPorDesconexion();
     }*/ //TODO: CORREGIR CUANDO VEAMOS EL TIEMPO DEL BROKER

    public void recibirDato(String dato, Heladera heladera) {
        // para el crontask
        heladera.setUltimaPublicacionTemperatura(LocalDateTime.now());
        Integer temperaturaActual = Integer.parseInt(dato);//recibo la temperatura, lo paso a int

        System.out.println("estoy en el receptor con el dato: " + temperaturaActual);
        this.evaluarTemperatura(temperaturaActual, heladera);
    }

    public void evaluarTemperatura(Integer temperaturaActual, Heladera heladera) {
        System.out.println("estoy evaluando: " + !this.tieneTemperaturaAceptable(temperaturaActual, heladera));
        if(!this.tieneTemperaturaAceptable(temperaturaActual, heladera)){//si no es aceptable
            System.out.println("Voy a desactivar la heladera ");
            receptorController.alarmaTemperatura(heladera);//hago la alarma
        }
    }

}
