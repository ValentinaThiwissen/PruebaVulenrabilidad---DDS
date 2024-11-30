package Domain.broker;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.*;
import Domain.models.entities.tarjeta.SolicitudApertura;
import Domain.models.entities.tarjeta.TarjetaDeHeladeras;
import Domain.models.entities.tarjeta.TarjetaDistribucionViandas;
import Domain.models.repository.ColaboradorRepository;
import Domain.models.repository.HeladeraRepository;
import Domain.models.repository.TarjetaDistribucionViandaRepository;
import Domain.models.repository.TarjetaHeladerasRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.mail.Session;
import java.util.List;

public class App {
    public static void main(String[] args)throws MqttException {

        SensorTemperatura sensorTemperatura = new SensorTemperatura();
        BrokerHeladera unBroker = new BrokerHeladera();

        sensorTemperatura.setBroker(unBroker);
        MyCustomMessageReceptor receptor = new MyCustomMessageReceptor();
        unBroker.connect(unBroker.getBroker());

        unBroker.subscribe("heladera/HeladeraVal/temperatura", receptor);
        sensorTemperatura.publicarTemperatura("heladera/HeladeraVal/temperatura", 100); /*CAMBIAR A HELADERA CREADA EN LA ZONA DEL TÉCNICO*/

        try {
            Thread.sleep(2000); // Esperar 2 segundos para asegurar la recepción
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

