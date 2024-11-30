package Domain.models.entities.validadorcontrasenia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ValidadorContrasenea {
    private List<ValidacionContrasenas> validaciones;

    public ValidadorContrasenea() {
        this.validaciones = new ArrayList<ValidacionContrasenas>();
    }

    public Boolean validarContrasena(String contrasenia) throws IOException {
        for (ValidacionContrasenas validacion : validaciones) {
            validacion.esValida(contrasenia);
        }
        return true;
    }
    public void agregarFormaDeValidacion(ValidacionContrasenas validacion){
        validaciones.add(validacion);
    }
}
