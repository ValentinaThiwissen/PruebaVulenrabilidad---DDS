package Domain.broker;

import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.ReceptorMovimiento;
import Domain.models.entities.heladera.ReceptorSolicitud;
import Domain.models.entities.heladera.ReceptorTemperatura;
import Domain.models.entities.tarjeta.SolicitudApertura;
import Domain.models.repository.HeladeraRepository;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MyCustomMessageReceptor implements IMqttMessageListener {
    HeladeraRepository heladeraRepository = new HeladeraRepository();

    public MyCustomMessageReceptor() {
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        System.out.println("Message recived from topic " + topic + ": " + mqttMessage.toString());

        String[] topicEnPartes = topic.split("/");
        String tipoTopic = topicEnPartes[0];
        System.out.println(tipoTopic);
        String nombreHeladera = topicEnPartes[1];
        System.out.println(nombreHeladera);
        String tipo = topicEnPartes[2];
        System.out.println(tipo);
        String mensaje = new String(mqttMessage.getPayload());
        System.out.println(mensaje);

        if(tipoTopic.equals("heladera")){
            Heladera heladera = heladeraRepository.buscarPorNombre(nombreHeladera);
            System.out.println(heladera.getNombreHeladera());

            switch(tipo){
                case "temperatura":
                    System.out.println("Estoy en switch temperatura");
                    heladera.setReceptorTemperatura(new ReceptorTemperatura());
                    ReceptorTemperatura receptorTemperatura = heladera.getReceptorTemperatura();
                    receptorTemperatura.recibirDato(mensaje, heladera);
                    break;

                case "movimiento":
                    System.out.println("Estoy adentro del switch");
                    heladera.setReceptorMovimiento(new ReceptorMovimiento());
                    ReceptorMovimiento receptorMovimiento = heladera.getReceptorMovimiento();
                    receptorMovimiento.recibirDato(mensaje,heladera);
                    break;

                case "apertura":
                    ReceptorSolicitud receptorSolicitud = heladera.getReceptorSolicitud();
                    receptorSolicitud.recibirDato(mensaje, heladera);
                    break;

            }
        }
    }

}

