package Domain.models.entities.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties prop = new Properties();
    private static Config instancia;

    public static final String RUTA_EXPORTACION = "";

    public static final Integer TIEMPO_NOTIFICACIONES = 21600000;
    public static final Integer TIEMPO_REPORTES = 604800000;
    public static final String DOWNLOAD = "";


    private Config() { //porque es un Singleton
    }

    public static Config obtenerInstancia() { // Singleton
        if (instancia == null){
            instancia = new Config();
        }
        return instancia;
    }

    public String obtenerDelConfig(String key) {
        try (InputStream input = Config.class.getResourceAsStream("/config.properties")) {
            prop.load(input);
            return prop.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}