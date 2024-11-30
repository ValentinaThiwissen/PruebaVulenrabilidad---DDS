package Domain.models.entities.validadorcontrasenia;

import java.io.IOException;

public interface ValidacionContrasenas  {
    Void esValida(String contrasena) throws IOException;
}