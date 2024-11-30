package Domain.controller;

import Domain.DTO.UsuarioDTO;
import Domain.models.entities.Usuario.TipoPermiso;
import Domain.models.entities.Usuario.TipoRol;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.formasDeColaborar.Oferta;
import Domain.models.entities.formulario.Formulario;
import Domain.models.entities.heladera.Incidente;
import Domain.models.entities.reporte.reportes.ReporteExportable;
import Domain.models.entities.tecnico.VisitaTecnica;
import Domain.models.repository.*;
import Domain.utils.ICrudViewsHandler;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

import java.util.*;
import java.util.stream.Collectors;

public class AdministrarController  implements ICrudViewsHandler, WithSimplePersistenceUnit {
    UsuarioRepository usuarioRepository;
    RolRepository rolRepository;
    IncidenteRepository incidenteRepository;
    OfertaRepository ofertaRepository;
    ColaboradorRepository colaboradorRepository;
    FormularioRepository formularioRepository;
    ReporteRepository reporteRepository;
    VisitasTecnicasRepository visitasTecnicasRepository;
    public AdministrarController(UsuarioRepository usuarioRepository, RolRepository rolRepository, IncidenteRepository incidenteRepository, OfertaRepository ofertaRepository, ColaboradorRepository colaboradorRepository, FormularioRepository formularioRepository, VisitasTecnicasRepository visitasTecnicasRepository){
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.incidenteRepository = incidenteRepository;
        this.ofertaRepository = ofertaRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.formularioRepository = formularioRepository;
        this.visitasTecnicasRepository= visitasTecnicasRepository;;
        this.reporteRepository = new ReporteRepository();

    }

    @Override
    public void index(Context context) {

    }

    @Override
    public void show(Context context) {

    }

    @Override
    public void create(Context context) {
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

        String nombreUsuario = usuario.getNombreDeUsuario();

        Map<String, Object> model = new HashMap<>();
        model.put("nombreUsuario", nombreUsuario);

        context.render("administrador.hbs", model);
    }

    @Override
    public void save(Context context) {
        context.render("administrador.hbs");
    }

    public void createVisualizarHeladeras(Context context){
        context.render("visualizarHeladeras.hbs");
    }

    public void createVisualizarOfertas(Context context){
        Long id = context.sessionAttribute("usuario_id");
        if (id == null) {
            context.redirect("/inicioSesion");
            return;
        }
        Usuario usuario = usuarioRepository.buscarPorID(id);
        if (usuario == null) {
            context.status(404).result("Usuario no encontrado");
        }

        Map<String, Object> model = new HashMap<>();

        Colaborador colaborador = colaboradorRepository.buscarPorUsuario(usuario);
        Set<Oferta> ofertas = colaborador.getOfertasEmpresa();

        if(ofertas == null){
            context.status(404).result("No hay ofertas realizados");
        }
        model.put("oferta", ofertas);
        context.render("visualizarOfertas.hbs", model);
    }



    public void createVisualizarIncidentes(Context context){
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
        if (usuario.getRol().getTipo()  == TipoRol.ADMIN && usuario.getRol().getPermisos().contains(TipoPermiso.VISUALIZARALERTAS))
        {
            List<Incidente> incidentes = incidenteRepository.buscarTodos();

            Map<String, Object> model = new HashMap<>();
            model.put("incidente", incidentes);
            context.render("visualizarIncidente.hbs", model);}
    }

    public void createVisualizarFormularios(Context context){
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
        if (usuario.getRol().getTipo()  == TipoRol.ADMIN && usuario.getRol().getPermisos().contains(TipoPermiso.GESTIONARFORMULARIOS))
        {
            Map<String, Object> model = new HashMap<>();
            List<Formulario> formularios = formularioRepository.buscarTodos();
            if (formularios == null || formularios.isEmpty()) {
                context.status(404).result("No hay formularios disponibles");
                return;
            }

            model.put("formularios", formularios);
            context.render("gestionarFormularios.hbs", model);
        }

    }

    public void createGestionarUsuario(Context context){
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

        if (usuario.getRol().getTipo()  == TipoRol.ADMIN && usuario.getRol().getPermisos().contains(TipoPermiso.GESTIONARFORMULARIOS)) {
            List<Usuario> usuarios = usuarioRepository.todos();

            if(usuarios == null){
                context.status(404).result("Usuarios es null");
            }


            List<UsuarioDTO> usuariosDTO = new ArrayList<>();

            for(int i=0; i< usuarios.size(); i++){
                Usuario usuario1 = usuarios.get(i);
                if(usuario1.getHabilitado()){
                    UsuarioDTO dto = new UsuarioDTO();
                    dto.setId(String.valueOf(usuario1.getId()));
                    dto.setHabilitado(String.valueOf(usuario1.getHabilitado()));
                    dto.setNombreUsuario(usuario1.getNombreDeUsuario());
                    dto.setContrasenia(usuario1.getContrasenia());
                    dto.setRol(usuario1.getRol().getTipo().name());
                    dto.setPermisoRegistrarPersonaVulnerable(usuario1.getRol().tienePermiso(TipoPermiso.REGISTROPERSONAVULNERABLE));
                    dto.setPermisoDonacionMonetaria(usuario1.getRol().tienePermiso(TipoPermiso.DONACIONMONETARIA));
                    dto.setPermisoDonacionViandas(usuario1.getRol().tienePermiso(TipoPermiso.DONACIONVIANDAS));
                    dto.setPermisoDistribucionViandas(usuario1.getRol().tienePermiso(TipoPermiso.DISTRIBUCIONVIANDAS));
                    dto.setPermisoOfrecerProductos(usuario1.getRol().tienePermiso(TipoPermiso.OFRECERPRODUCTOS));
                    dto.setPermisoEncargarseHeladera(usuario1.getRol().tienePermiso(TipoPermiso.ENCARGARSEHELADERA));
                    usuariosDTO.add(dto);
                }
            }
            if(usuariosDTO == null){
                context.status(404).result("Usuarios es null");
            }

            Map<String, Object> model = new HashMap<>();
            model.put("usuario", usuariosDTO);
            context.render("gestionarUsuarios.hbs", model);


        }
    }

    public void deleteUsuario(Context context){

        String idUsuario = context.formParam("usuarioId");
        if (idUsuario == null) {
            context.status(400).result("ID de usuario no proporcionado");
            return;
        }

        Long usuarioId = Long.valueOf(idUsuario);
        Usuario usuario = usuarioRepository.buscarPorID(usuarioId);

        if (usuario == null) {
            context.status(404).result("Usuario no encontrado");
            return;
        }
        if(usuarioId != 1){
            usuario.setHabilitado(false);
            usuarioRepository.updateUsuario(usuario);
        }



        context.redirect("/gestionarUsuarios");

    }


    public void createVisualizarVisitasTecnicas(Context context) {
        /* Long id = context.sessionAttribute("usuario_id");
        if (id == null) {
            context.redirect("/inicioSesion");
            return;
        }

        Usuario usuario = usuarioRepository.buscarPorID(id);
        if (usuario == null) {
            context.status(404).result("Usuario no encontrado");
            return;
        }
        if (usuario.getRol().getTipo()  == TipoRol.ADMIN && usuario.getRol().getPermisos().contains(TipoPermiso.VISUALIZARVISITASTECNICAS))
        {*/
        Map<String, Object> model = new HashMap<>();
        List<VisitaTecnica> visitasTecnicas = this.visitasTecnicasRepository.buscarTodos();
        List<VisitaTecnica> visitasTecnicasFiltradas = visitasTecnicas.stream()
                                                        .filter(VisitaTecnica::getSolucionadoTrabajo)
                                                        .toList();


        model.put("visitasTecnicas", visitasTecnicasFiltradas);
        context.render("visualizarVisitasTecnicas.hbs", model);
        /*  }
        context.status(404).result("Usuario sin permiso");*/
    }

    public void createContacto(Context context){
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

        context.render("contacto.hbs");
    }

    public void saveContacto(Context context){
        context.render("/contacto.hbs");
    }

    public void createPreguntasFrecuentes(Context context){
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

        context.render("preguntasFrecuentes.hbs");
    }

    public void createTermino(Context context){
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

        context.render("termino.hbs");
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