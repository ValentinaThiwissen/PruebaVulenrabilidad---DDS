package Domain.Validaciones.ValidacionesContrasenas;

import java.io.IOException;

public interface ValidacionContrasenas {
    Void esValida(String contrasena) throws IOException;
}
