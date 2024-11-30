package Domain.controller.ColaboracionController;

import Domain.DTO.PuntoGeograficoHeladeraDTO;
import Domain.models.entities.Ubicacion.*;
import Domain.models.entities.Usuario.Rol;
import Domain.models.entities.Usuario.TipoPermiso;
import Domain.models.entities.Usuario.TipoRol;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.colaborador.MedioDePago;
import Domain.models.entities.colaborador.TipoMedioPago;
import Domain.models.entities.formasDeColaborar.*;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.Modelo;
import Domain.models.entities.personaVulnerable.PersonaVulnerable;
import Domain.models.entities.personaVulnerable.TipoDocumento;
import Domain.models.entities.tarjeta.TarjetaDeHeladeras;
import Domain.models.entities.vianda.Comida;
import Domain.models.entities.vianda.Vianda;
import Domain.models.repository.*;
import Domain.utils.ICrudViewsHandler;
import com.google.gson.Gson;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import org.hibernate.annotations.DialectOverride;

import java.time.LocalDate;
import java.util.*;

public class FormaColaboracionJController implements ICrudViewsHandler, WithSimplePersistenceUnit {
    UsuarioRepository usuarioRepository;
    ColaboradorRepository colaboradorRepository;
    OfertaRepository ofertaRepository;
    FormaColaboracionRepository formaColaboracionRepository;
    HeladeraRepository heladeraRepository;


    public  FormaColaboracionJController(UsuarioRepository usuarioRepository,ColaboradorRepository colaboradorRepository, OfertaRepository ofertaRepository, FormaColaboracionRepository formaColaboracionRepository, HeladeraRepository heladeraRepository) {
        this.usuarioRepository = usuarioRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.ofertaRepository = ofertaRepository;
        this.formaColaboracionRepository = formaColaboracionRepository;
        this.heladeraRepository = heladeraRepository;
    }
    @Override
    public void index(Context context) {


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

    @Override
    public void show(Context context) {

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

        List<PuntoGeograficoHeladeraDTO> heladeras = this.heladeraRepository.buscarTodosPorPuntoGeograficoActiva();

        if (heladeras == null || heladeras.isEmpty()) {
            context.status(404).result("No se encontraron heladeras");
            return;
        }
        context.result(new Gson().toJson(heladeras));

        Map<String, Object> model = new HashMap<>();
        model.put("heladeras", heladeras);

        String nombreUsuario = usuario.getNombreDeUsuario();

        model.put("nombreUsuario", nombreUsuario);

        context.render("personaJuridica.hbs", model);
    }



    public void create(Context context) {
        Map<String, Object> model = new HashMap<>();


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

        Rol rol = usuario.getRol();

        // Variables booleanas para la vista
        boolean puedeDonarDinero       =  rol.getTipo().equals(TipoRol.PERSONA_JURIDICA) && rol.getPermisos().contains(TipoPermiso.DONACIONMONETARIA);
        boolean puedeDistribuirVianda  = false;
        boolean puedeDonarVianda       = false;
        boolean puedeRegistrarPersonas = false;
        boolean puedeEncargarHeladera  = rol.getTipo().equals(TipoRol.PERSONA_JURIDICA) && rol.getPermisos().contains(TipoPermiso.ENCARGARSEHELADERA);
        boolean puedeOfrecerProductos  = rol.getTipo().equals(TipoRol.PERSONA_JURIDICA) && rol.getPermisos().contains(TipoPermiso.OFRECERPRODUCTOS);

        // Pasamos las variables al contexto
        model.put("puedeDonarDinero", puedeDonarDinero);
        model.put("puedeDistribuirVianda", puedeDistribuirVianda);
        model.put("puedeDonarVianda", puedeDonarVianda);
        model.put("puedeRegistrarPersonas", puedeRegistrarPersonas);
        model.put("puedeEncargarHeladera", puedeEncargarHeladera);
        model.put("puedeOfrecerProductos", puedeOfrecerProductos);


        context.render("personaJuridica.hbs", model);
    }


    @Override
    public void save(Context context) {
        String action = context.formParam("action");
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

    }
}

