package Domain.Validaciones.ValidacionesContrasenas;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class ContieneNumero implements  ValidacionContrasenas{
    private List<Character> numeros = Arrays.asList('1','2','3','4','5','6','7','8','9','0');

    @Override
    public Void esValida(String contrasena) {
        for(int i = 0; i<contrasena.length(); i++){
            char caracter = contrasena.charAt(i);
            if(!numeros.contains(caracter)){
                JOptionPane.showMessageDialog(null, "Error: La contraseña tiene que tener un numero ", "Error", JOptionPane.ERROR_MESSAGE);

            }
        }
        return null;
    }
}
