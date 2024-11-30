package Domain.controller.LogInController;

import Domain.models.entities.Usuario.TipoRol;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.validadorcontrasenia.ValidadorContrasenea;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.colaborador.TipoColaborador;
import Domain.models.repository.ColaboradorRepository;
import Domain.models.repository.UsuarioRepository;
import Domain.utils.ICrudViewsHandler;
import java.io.IOException;
import io.javalin.http.Context;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LogInController implements ICrudViewsHandler {
    private ColaboradorRepository colaboradorRepository;
    private UsuarioRepository usuarioRepository;
    private ValidadorContrasenea validador;

    public LogInController(UsuarioRepository usuarioRepository, ColaboradorRepository colaboradorRepository, ValidadorContrasenea validador){
        this.usuarioRepository = usuarioRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.validador = new ValidadorContrasenea();
    }
    @Override
    public void index(Context context) {
        Map <String, Object> model = new HashMap<>();

        String errorType = context.queryParam("error");
        if (errorType != null) {
            switch (errorType) {
                case "unregistered":
                    model.put("errorMessage", "Usuario no registrado.");
                    break;
                case "invalid_password":
                    model.put("errorMessage", "Contraseña inválida.");
                    break;
                default:
                    model.put("errorMessage", "Ocurrió un error al intentar iniciar sesión.");
                    break;
            }
        }

        context.render("inicioSesion.hbs", model);
    }

    public void validar(Context context) {
        String nombre = context.formParam("usuario");
        String contrasenia = context.formParam("contrasenia");

        Usuario usuario = this.usuarioRepository.buscarPorNombreUsuario(nombre);

        Map<String, Object> model = new HashMap<>();

        // Verificar si el usuario está registrado
        if (usuario == null) {
            model.put("errorUsuario", "No hemos podido encontrar ninguna cuenta con ese nombre de usuario.");
            context.render("inicioSesion.hbs", model);
            return;
        }
        if(usuario.getHabilitado()){
            String contraseniaDB = usuario.getContrasenia();

            // Verificar la contraseña
            if (contrasenia.equals(contraseniaDB)) {
                context.sessionAttribute("usuario_id", usuario.getId());
                context.cookie("usuario", usuario.getNombreDeUsuario(), 604800);
                // Cookie que expira en 7 días


                if (usuario.getRol().getTipo() == TipoRol.PERSONA_HUMANA) {
                    context.redirect("/personaHumana");
                } else if (usuario.getRol().getTipo() == TipoRol.PERSONA_JURIDICA) {
                    context.redirect("/personaJuridica"); //CAMBIAR DESPUES ES SOLO PRUEBA
                } else if (usuario.getRol().getTipo() == TipoRol.ADMIN) {
                    context.redirect("/administrador"); //CAMBIAR DESPUES ES SOLO PRUEBA
                }else if(usuario.getRol().getTipo()==TipoRol.TECNICO){
                    context.redirect("/visitaTecnica");
                }
            } else {
                model.put("errorContrasenia", "Contraseña incorrecta.");
                context.render("inicioSesion.hbs", model);
            }
        }else{
            model.put("errorUsuario", "El usuario fue eliminado del sistema.");
            context.render("inicioSesion.hbs", model);
        }


    }

    public void logout(Context context) {
        context.removeCookie("usuario");
        context.consumeSessionAttribute("usuario_id");
        context.redirect("/inicioSesion");
    }

    @Override
    public void show(Context context) {

    }

    @Override
    public void create(Context context) {

    }

    @Override
    public void save(Context context) {

    }

    @Override
    public void edit(Context context) {

    }

    @Override
    public void update(Context context) {

    }

    @Override
    public void delete(Context context) {

    }

}
