package Domain.broker;
import Domain.models.entities.heladera.*;
import Domain.models.repository.HeladeraRepository;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

@Data
@Getter
@Setter
public class BrokerHeladera{

    private String broker= "tcp://broker.hivemq.com:1883";;
    private MqttClient client;

    /*private String broker
    IMqttClient client;
    public BrokerHeladera(String broker) throws MqttException {
        this.broker = broker;
    }*/

    public void connect(String clientId) {
        try {
            MemoryPersistence persistence = new MemoryPersistence();

            this.client = new MqttClient(broker, clientId, persistence);

            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);

            client.connect(connectOptions);
            System.out.println("Conectando al broker " + broker);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        /* client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connection lost! " + cause.getMessage());
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                HeladeraRepository heladeraRepository = HeladeraRepository.getInstancia(); // Acceder al Singleton
                MyCustomMessageReceptor m = new MyCustomMessageReceptor(heladeraRepository);
                m.messageArrived(s, mqttMessage);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("Delivery complete!");
            }
        });*/

    }



    public void disconnect() throws MqttException {
        client.disconnect();
        System.out.println("Disconnected");
    }

    public void publish(String topic, String content) {
        try {
            MqttMessage message = new MqttMessage(content.getBytes());
            //message.setQos(2);
            client.publish(topic, message);
            System.out.println("Message published");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void subscribe(String topic, IMqttMessageListener messageListener) {
        try {
            // Suscripción al topic con el listener
            client.subscribe(topic, messageListener);
            System.out.println("Subscribed to topic: " + topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /*
    public void subscribe(String topic) {
        try {
            client.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        System.out.println("Subscribed to topic: " + topic);
    }*/
}