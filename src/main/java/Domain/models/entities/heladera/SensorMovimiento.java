package Domain.models.entities.heladera;

import Domain.broker.BrokerHeladera;
import Domain.services.notificadorRobosApi.NotificadorRobos;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.paho.client.mqttv3.MqttException;

@Setter
@Getter
public class SensorMovimiento {
    private BrokerHeladera broker;

    public void publicarMovimiento(String topico, Boolean movimiento){
        String movimientoParseado = movimiento.toString();
        broker.publish(topico, movimientoParseado);
    }

}
