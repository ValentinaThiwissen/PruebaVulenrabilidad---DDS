package Domain.controller.UsuarioController;

import Domain.models.entities.validadorcontrasenia.ValidadorContrasenea;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.repository.ColaboradorRepository;
import Domain.models.repository.RolRepository;
import Domain.models.repository.UsuarioRepository;
import Domain.utils.ICrudViewsHandler;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class UsuarioController implements ICrudViewsHandler {
    private UsuarioRepository usuarioRepository;
    private ColaboradorRepository colaboradorRepository;
    private ValidadorContrasenea validadorDeContrasenia;
    private RolRepository rolRepository;


    public UsuarioController(UsuarioRepository usuarioRepository, ColaboradorRepository colaboradorRepository, ValidadorContrasenea validadorDeContrasenia, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.validadorDeContrasenia = validadorDeContrasenia;
        this.rolRepository = rolRepository;

    }

    @Override
    public void index(Context context) {
        Map<String, Object> model = new HashMap<>();

        String errorType = context.queryParam("error");
        if (errorType != null) {
            switch (errorType) {
                case "unregistered":
                    model.put("errorMessage", "Usuario no registrado.");
                    break;
                case "encryption_error":
                    model.put("errorMessage", "Error al encriptar la contraseña. Inténtelo nuevamente.");
                    break;
                case "invalid_password":
                    model.put("errorMessage", "Contraseña inválida.");
                    break;
                default:
                    model.put("errorMessage", "Ocurrió un error al intentar iniciar sesión.");
                    break;
            }
        }

        context.render("login.hbs", model);
    }


    public void validar(Context context) {
        String nombre = context.formParam("nombre");
        String contrasenia = context.formParam("contrasenia");
        String contraseniaEncriptadaDB = " ";
        Usuario usuario = this.usuarioRepository.buscarPorNombreUsuario(nombre);

        // Verificar si el usuario está registrado
        if(usuario == null) {
            context.redirect("/login?error=unregistered");
            return;
        }


    }

    // INICIO
    @Override
    public void show(Context context) {
        context.render("index.hbs");
    }

    //SIGNUP
    @Override
    public void create(Context context) {
        Map<String, Object> model = new HashMap<>();

        String errorType = context.queryParam("error");
        if (errorType != null) {
            switch (errorType) {
                case "complexity":
                    model.put("errorMessage", "La contraseña debe tener mayúsculas, minúsculas, números y símbolos.");
                    break;
                case "length":
                    model.put("errorMessage", "La contraseña no cumple con la longitud requerida.");
                    break;
                case "invalid_password":
                    model.put("errorMessage", "Contraseña inválida.");
                    break;
                case "registration_error":
                    model.put("errorMessage", "Error al intentar registrar al usuario. Inténtelo nuevamente.");
                    break;
                default:
                    model.put("errorMessage", "Ocurrió un error al intentar registrarse.");
                    break;
            }
        }

        context.render("signup.hbs", model);
    }


    @Override
    public void save(Context context) {

    }


    //NADA
    @Override
    public void edit(Context context) {
        //TODO
    }

    @Override
    public void update(Context context) {
        //TODO
    }

    @Override
    public void delete(Context context) {
        //TODO
    }
}
