package Domain.controller.MigradorController;

import Domain.models.entities.MigradorDeDonantes.LectorCSV;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.notificacion.Notificacion;
import Domain.models.entities.notificacion.NotificarEmail;
import Domain.models.repository.ColaboradorRepository;
import Domain.models.repository.UsuarioRepository;
import Domain.utils.ICrudViewsHandler;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class MigradorController implements ICrudViewsHandler, WithSimplePersistenceUnit {

    ColaboradorRepository colaboradorRepository;
    UsuarioRepository usuarioRepository = new UsuarioRepository();

    public MigradorController(ColaboradorRepository repositorio){
        colaboradorRepository = repositorio;

    }

    @Override
    public void save(Context context) {
        try {
            InputStream fileContent = context.uploadedFile("archivo").content();
            Path tempFile = Files.createTempFile("temp-", ".csv");

            Files.copy(fileContent, tempFile, StandardCopyOption.REPLACE_EXISTING);

            LectorCSV lector = new LectorCSV(tempFile.toString());
            List<Colaborador> colaboradores = lector.leerUsuarios();  // O la función correspondiente para procesar

            for (Colaborador colaborador : colaboradores) {
                beginTransaction();
                colaboradorRepository.guardar(colaborador);
                commitTransaction();
            }

            List<Colaborador> usuariosNuevos = lector.obtenerUsuariosNuevos(colaboradores);

            NotificarEmail notificador = new NotificarEmail();

            for(Colaborador colaborador : colaboradores){
                Usuario usuario = usuarioRepository.generarUsuarioPara(colaborador);
                colaborador.setUsuario(usuario);
                beginTransaction();
                usuarioRepository.guardar(usuario);
                commitTransaction();
                Notificacion notificacion = new Notificacion();
                notificacion.setDestinatario(colaborador);
                notificacion.setMensaje("Se migro tu usuario a nuestro nuevo sistema, tu nuevo usuario es " +
                        usuario.getNombreDeUsuario() +
                        " y tu nueva contraseña es " +
                        usuario.getContrasenia());
                notificacion.setAsunto("Migracion de usuario a nueva pagina");
                notificador.enviarMail(notificacion);
            }

            // Enviar respuesta al cliente
            context.status(200).result("Archivo migrado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            context.status(500).result("Error al migrar el archivo.");
        }
    }


    @Override
    public void index(Context context){
        //TODO
    }

    @Override
    public void show(Context context){
        context.render("cargaMasiva.hbs");
    }

    @Override
    public void update(Context context) {
        //TODO
    }
    @Override
    public void delete(Context context) {
        //TODO
    }
    @Override
    public void edit(Context context) {
        //TODO
    }
    @Override
    public void create(Context context) {
        //TODO
    }

}
