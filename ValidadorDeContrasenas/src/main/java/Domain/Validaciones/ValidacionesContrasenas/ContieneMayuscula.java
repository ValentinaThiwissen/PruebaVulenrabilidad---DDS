package Domain.Validaciones.ValidacionesContrasenas;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class ContieneMayuscula implements ValidacionContrasenas{
    private List<Character> mayusculas = Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');

    @Override
    public Void esValida(String contrasena) {

        for(int i = 0; i<contrasena.length(); i++){
            char caracter = contrasena.charAt(i);
            if(mayusculas.contains(caracter)){
                JOptionPane.showMessageDialog(null, "Error: La contraseña tiene que contener una mayuscula ", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }
}
