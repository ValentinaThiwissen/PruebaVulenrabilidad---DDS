package Domain;

import java.io.IOException;
import java.util.Objects;

public class Usuario {
    private String nombreUsuario;
    private static String contrasena;
    public static Boolean esMiContrasena(String contrasenaIngresada){
        return Objects.equals(contrasena, contrasenaIngresada);
    }
    public Boolean contrasenaValidaPara(ValidadorContrasenea validador) throws IOException {
        try {
            return validador.validarContrasena(contrasena);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void nombreUsuario(String nombre) {
        nombreUsuario = nombre;
    }

    public void contrasenaUsuario(String contrasenaIngresaada) {
        contrasena = contrasenaIngresaada;
    }
}
