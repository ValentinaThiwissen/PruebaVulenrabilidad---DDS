package Domain.models.entities.validadorcontrasenia;

import javax.swing.*;
import java.io.IOException;

public class CumpleLongitud implements  ValidacionContrasenas{
    private Integer cantidadMinima;

    @Override
    public Void esValida(String contrasena) throws IOException {
        Integer longitud = contrasena.length();
        if(longitud <= cantidadMinima){
            JOptionPane.showMessageDialog(null, "Error: La contraseña tiene que tener un minimo de" + cantidadMinima, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
