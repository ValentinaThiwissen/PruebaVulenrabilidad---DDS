package Domain.Validaciones.ValidacionesContrasenas;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class ContieneCaracterEspecial implements  ValidacionContrasenas{
    private List<Character> especiales = Arrays.asList('-','_','.',',',';',':');
    @Override
    public Void esValida(String contrasena) {

        for(int i = 0; i<contrasena.length(); i++){
            char caracter = contrasena.charAt(i);
            if(!especiales.contains(caracter)){
                JOptionPane.showMessageDialog(null, "Error: La contraseña tiene que tener un caracter especial ", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }
}
