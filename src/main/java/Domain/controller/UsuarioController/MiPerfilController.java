package Domain.controller.UsuarioController;

import Domain.DTO.ColaboradorDTO;
import Domain.models.entities.Usuario.TipoPermiso;
import Domain.models.entities.Usuario.TipoRol;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.colaborador.TipoColaborador;
import Domain.models.entities.validadorcontrasenia.ValidadorContrasenea;
import Domain.models.repository.ColaboradorRepository;
import Domain.models.repository.RolRepository;
import Domain.models.repository.UsuarioRepository;
import Domain.utils.ICrudViewsHandler;
import io.javalin.http.Context;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiPerfilController implements ICrudViewsHandler {
    private UsuarioRepository usuarioRepository;
    private ColaboradorRepository colaboradorRepository;

    public MiPerfilController(ColaboradorRepository colaboradorRepository,UsuarioRepository usuarioRepository ) {
        this.colaboradorRepository = colaboradorRepository;
        this.usuarioRepository = usuarioRepository;

    }

    @Override
    public void index(Context context){
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
        ColaboradorDTO colaboradorDTO = new ColaboradorDTO();
        colaboradorDTO.setNombre(colaborador.getNombre());
        colaboradorDTO.setTipoColaborador(colaborador.getTipoColaborador().name());
        colaboradorDTO.setNombreUsuario(usuario.getNombreDeUsuario());

        if (colaborador.getUnaDireccion() != null){
            colaboradorDTO.setUnaDireccion(colaborador.getUnaDireccion().getCalle());
        }

        colaboradorDTO.setMail(colaborador.getMail());
        
        if (usuario.getRol().getTipo() == TipoRol.PERSONA_HUMANA) {
            colaboradorDTO.setApellido(colaborador.getApellido());
            colaboradorDTO.setTipoDni(colaborador.getTipoDni().name());
            colaboradorDTO.setNroDni(colaborador.getNroDni().toString());
            colaboradorDTO.setNacimiento(colaborador.getNacimiento().toString());

        }else if (usuario.getRol().getTipo() == TipoRol.PERSONA_JURIDICA){
            colaboradorDTO.setRazonSocial(colaborador.getRazonSocial());
            colaboradorDTO.setTipoJuridico(colaborador.getTipoJuridico().toString());
        }


        Map<String, Object> model = new HashMap<>();
        boolean nombre = true;
        boolean apellido = false;
        boolean tipoColaborador =true;
        boolean nombreUsuario =true;
        //boolean direccion =true;
        boolean mail =true;
        boolean nacimiento =false;
        boolean tipoJuridico =false;
        boolean razonSocial =false;
        //boolean tipoDni =false;
        //boolean nroDni =false;

        if(usuario.getRol().getTipo() == TipoRol.PERSONA_HUMANA){
            apellido = true;
            nacimiento =true;
            //tipoDni = true;
            //nroDni = true;
        } else if (usuario.getRol().getTipo() == TipoRol.PERSONA_JURIDICA) {
            tipoJuridico = true;
            razonSocial =true;
        }

        // Pasamos las variables al contexto
        model.put("nombre", nombre);
        model.put("apellido", apellido);
        model.put("tipoColaborador", tipoColaborador);
        model.put("nombreUsuario", nombreUsuario);
        //model.put("direccion", direccion);
        model.put("mail", mail);
        model.put("nacimiento", nacimiento);
        model.put("tipoJuridico", tipoJuridico);
        model.put("razonSocial", razonSocial);
        //model.put("tipoDni", tipoDni);
        //model.put("nroDni", nroDni);
        model.put("colaborador", colaboradorDTO);

        context.render("miPerfil.hbs",model);
    }


/**
 public void logout(Context context) {
 context.consumeSessionAttribute("usuario_id");
 context.redirect("/login");
 }
 //LOGIN
 */


public void validar(Context context) {
}

    // INICIO
    @Override
    public void show(Context context) {
        /*String usuarioId = context.sessionAttribute("usuario_id");
        if (usuarioId != null) {
           Colaborador usuario = this.colaboradorRepository.buscarPorUsuario(usuarioId);
            if (usuario != null) {
                Map<String, Object> model = new HashMap<>();
                model.put("usuario", usuario);
                context.render("miPerfil.hbs", model);
            } else {
                context.status(404).result("Usuario no encontrado");
            }
        } else {
            context.render("404.hbs");
        }**/

    }

    //SIGNUP
    @Override
    public void create(Context context) {
        context.render("miPerfil.hbs");
    }


    @Override
    public void save(Context context) {

    }


    //NADA
    @Override
    public void edit(Context context) {
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

        // Pasar los datos del colaborador al contexto para que se muestren en la vista
        context.attribute("colaborador", colaborador);
        context.render("editarPerfil.hbs");
    }

    @Override
    public void update(Context context) {
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

        // Obtener los valores actuales
        String nombreActual = colaborador.getNombre();
        String apellidoActual = colaborador.getApellido();
        String emailActual = colaborador.getMail();
        TipoColaborador tipoColaboradorActual = colaborador.getTipoColaborador();

        // Obtener los valores del formulario
        String nombre = context.formParam("nombre");
        String apellido = context.formParam("apellido");
        String tipoColaborador = context.formParam("tipoColaborador");
        String email = context.formParam("email");

        // Actualizar solo los campos que han cambiado
        colaborador.setNombre(nombre.isEmpty() ? nombreActual : nombre);
        colaborador.setApellido(apellido.isEmpty() ? apellidoActual : apellido);
        colaborador.setMail(email.isEmpty() ? emailActual : email);


        try {
            colaboradorRepository.actualizar(colaborador);
            context.redirect("/perfil");
        } catch (Exception e) {
            e.printStackTrace();
            context.status(500).result("Ha ocurrido un error en la actualizacion de los datos.");
        }
    }


    @Override
    public void delete(Context context) {
        //TODO
    }
}


