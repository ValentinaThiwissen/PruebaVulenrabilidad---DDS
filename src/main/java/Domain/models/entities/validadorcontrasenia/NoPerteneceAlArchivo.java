package Domain.models.entities.validadorcontrasenia;

import javax.swing.*;
import java.io.*;

public class NoPerteneceAlArchivo implements ValidacionContrasenas{
    private File fichero = new File("10000WorstPasswords.txt");
    @Override
    public Void esValida(String contrasena) throws IOException, FileNotFoundException {
        FileReader lector = new FileReader(fichero);
        BufferedReader lectorLinea = new BufferedReader(lector);
        String linea = " ";

        while ((linea = lectorLinea.readLine()) != null) {
            if (linea.contains(contrasena)) {
                JOptionPane.showMessageDialog(null, "Error: La contraseña es demasiado debil ", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }
}
