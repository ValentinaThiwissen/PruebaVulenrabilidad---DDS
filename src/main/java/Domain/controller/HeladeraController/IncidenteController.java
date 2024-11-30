package Domain.controller.HeladeraController;

import Domain.controller.ColaboradorController.ColaboradorJuridicoController;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.Incidente;
import Domain.models.entities.heladera.TipoIncidente;
import Domain.models.repository.*;
import Domain.utils.ICrudViewsHandler;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import Domain.Exceptions.AccesDeniedException;
import Domain.models.entities.Usuario.Usuario;


import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.javalin.http.HttpStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class IncidenteController  implements ICrudViewsHandler, WithSimplePersistenceUnit {
    IncidenteRepository incidenteRepository;
    HeladeraRepository heladeraRepository;
    UsuarioRepository usuarioRepository;
    RolRepository rolRepository;
    ColaboradorRepository colaboradorRepository;
    private static final Logger logger = Logger.getLogger(IncidenteController.class.getName());

    public IncidenteController(IncidenteRepository incidenteRepository, HeladeraRepository heladeraRepository, UsuarioRepository usuarioRepository, RolRepository rolRepository, ColaboradorRepository colaboradorRepository){
        this.incidenteRepository = incidenteRepository;
        this.heladeraRepository = heladeraRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository =rolRepository;
        this.colaboradorRepository = colaboradorRepository;
    }
    @Override
    public void index(Context context) {
        List<Incidente> incidentes  = (List<Incidente>) incidenteRepository.buscarTodos();

        Map<String, Object> model = new HashMap<>();
        model.put("incidentes",incidentes);
        model.put("titulo", "Listado de incidentes");

        List<Heladera> heladeras = this.heladeraRepository.buscarTodos();
        model.put("heladeras", heladeras);

        context.render("reportarIncidente.hbs", model);
    }

    @Override
    public void show(Context context) {
        int id = Integer.valueOf(context.pathParam("id"));
        Optional<Incidente> incidente = this.incidenteRepository.buscar(Long.valueOf(context.pathParam("id")));


        if(incidente != null){
            context.json(incidente);
            Map<String, Object> model = new HashMap<>();
            model.put("incidente", incidente.get());


        }
        else {
            context.status(404).result("incidente no encontrado");// pongo un mensaje de erore
        }
    }

    @Override
    public void create(Context context) {
        List<Heladera> heladeras = this.heladeraRepository.buscarTodos();
        Map<String, Object> model = new HashMap<>();
        model.put("heladeras", heladeras);
        context.render("reportarIncidente.hbs", model);
    }

    @Override
    public void save(Context context) {

        /*  if(usuario == null || !rolRepository.tienePermiso(usuario.getRol().getId(), "crear_incidente")) {
            throw new AccesDeniedException();
        }**/
        Long id = context.sessionAttribute("usuario_id");
        if (id == null) {
            context.redirect("/inicioSesion");
            return;
        }
        Usuario usuario = usuarioRepository.buscarPorID(id);
        if (usuario == null) {
            context.status(404).result("Usuario no encontrado");
            return;
        }

        Colaborador colaborador = colaboradorRepository.buscarPorUsuario(usuario);

        if (colaborador == null) {
            context.status(404).result("Colaborador no encontrado");
            return;
        }

        //String nombreHeladera = context.formParam("heladera");
        //Heladera heladera = this.heladeraRepository.buscarPorNombre(nombreHeladera);
        Long heladeraId = Long.valueOf(context.formParam("heladera"));
        Heladera heladera = this.heladeraRepository.buscarPorID(heladeraId);

        if(heladera == null){
            context.status(213).result("Error no existe la heladera: "+ heladeraId );
        }else{

            Incidente incidente = new Incidente();
            incidente.setHeladeraIncidente(heladera);
            String observaciones = context.formParam("observaciones");
            incidente.setDescripcion(observaciones);
            incidente.setColaboradorQueReporto(colaborador);
            incidente.setTipoIncidente(TipoIncidente.FALLA);
            incidente.setFechaIncidente(LocalDate.now());
            incidente.setHoraIncidente(LocalDateTime.now());
            heladera.setEstaActiva(false);

            try {
                Part foto = context.req().getPart("foto");
                if (foto != null && foto.getSize() > 0) {
                    String uploadDir = "uploads"; //
                    File uploadFolder = new File(uploadDir);
                    if (!uploadFolder.exists()) {
                        uploadFolder.mkdirs();
                    }
                    String fileName = Paths.get(foto.getSubmittedFileName()).getFileName().toString();
                    Path filePath = Paths.get(uploadDir, fileName);
                    Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    //Files.copy(foto.getInputStream(), filePath);
                    incidente.setFotoIncidente(uploadDir + "/" + fileName);
                }

                beginTransaction();
                this.incidenteRepository.guardar(incidente);
                logger.info(String.format("Se guardo el incidente, su descripcion %s", observaciones));
                commitTransaction();
                Map<String, Object> model = new HashMap<>();
                // Redirigir después de guardarCanje
                //context.redirect("/visualizacionReportes");
                model.put("solicitudExitosa", true);
                model.put("reporteExitoso", "¡El reporte fue enviado con éxito!");
                context.render("reportarIncidente.hbs", model);


            } catch (IOException | ServletException e) {
                // Manejar errores de entrada/salida o de Servlet
                e.printStackTrace();
                context.status(500).result("Error al procesar el formulario o guardarCanje el archivo.");
            }
            //this.incidenteRepository.guardarCanje(incidente);
        }


    }



    @Override
    public void edit(Context context) {
        int id = Integer.valueOf(context.pathParam("id"));
        Optional<Incidente> posibleIncidente = this.incidenteRepository.buscar(Long.valueOf(context.pathParam("id")));

        Map<String, Object> model = new HashMap<>();
        model.put("incidente", posibleIncidente.get());
        model.put("edicion", true);


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



