package Domain;

import Domain.Validaciones.ValidacionesContrasenas.ValidacionContrasenas;

import java.io.IOException;
import java.util.List;

public class ValidadorContrasenea {
    private List<ValidacionContrasenas> validaciones;
    public Boolean validarContrasena(String contrasena) throws IOException {
        for (ValidacionContrasenas validacion : validaciones) {
            validacion.esValida(contrasena);
        }
        return true;
    }
    public void agregarFormaDeValidacion(ValidacionContrasenas validacion){
        validaciones.add(validacion);
    }
}
