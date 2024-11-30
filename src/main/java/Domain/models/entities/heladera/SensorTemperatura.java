package Domain.models.entities.heladera;


import Domain.broker.BrokerHeladera;
import Domain.models.entities.tarjeta.SolicitudApertura;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.paho.client.mqttv3.MqttException;

@Setter
@Getter

public class SensorTemperatura {
    private BrokerHeladera broker;

    public void publicarTemperatura(String topico, Integer temperatura){
        String temperaturaParseada = temperatura.toString();
        broker.publish(topico,temperaturaParseada);
    }

}
