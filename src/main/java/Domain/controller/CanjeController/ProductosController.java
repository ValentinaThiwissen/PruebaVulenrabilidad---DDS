package Domain.controller.CanjeController;

import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.formasDeColaborar.Oferta;
import Domain.models.entities.formasDeColaborar.Producto;
import Domain.models.entities.formasDeColaborar.Rubro;
import Domain.models.repository.*;
import Domain.utils.ICrudViewsHandler;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductosController implements ICrudViewsHandler {
    OfertaRepository ofertaRepository;
    RubroRepository rubroRepository;
    UsuarioRepository usuarioRepository;
    ColaboradorRepository colaboradorRepository;

    public ProductosController(OfertaRepository ofertaRepository, RubroRepository rubroRepository, UsuarioRepository usuarioRepository, ColaboradorRepository colaboradorRepository){
        this.ofertaRepository = ofertaRepository;
        this.rubroRepository = rubroRepository;
        this.usuarioRepository = usuarioRepository;
        this.colaboradorRepository = colaboradorRepository;

    }

    @Override
    public void index(Context context) {

        List<Oferta> ofertas = this.ofertaRepository.buscarTodos();

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

        Double puntosColaborador = colaborador.getPuntos();

        Map<String, Object> model = new HashMap<>();
        model.put("oferta", ofertas);
        model.put("puntos", puntosColaborador);

        context.render("puntos.hbs", model);
    }

    @Override
    public void show(Context context) {
        /*
        Optional<Oferta> posiblesOfertas = this.ofertaRepository.buscarPorID(Long.valueOf(context.pathParam("id"));

        //TODO

        if(posibleProductoBuscado.isEmpty()) {
            context.status(HttpStatus.NOT_FOUND);
            return;
        }
        Map<String, Object> model = new HashMap<>();
        model.put("oferta", posiblesOfertas.get());

        context.render("ofertas/detalle_ofertas.hbs", model);
*/
    }

    @Override
    public void create(Context context) {

        context.render("ofertas/formulario_producto.hbs");

    }

    @Override
    public void save(Context context) {

        Oferta oferta = new Oferta();
        Producto nuevoProducto = new Producto();
        Rubro rubro = new Rubro();
        rubro.setNombreRubro(context.formParam("nombreRubro"));
        oferta.setNombre(context.formParam("nombreOferta"));
        oferta.setRubro(rubro);
        oferta.setPuntosNecesarios(Integer.valueOf(context.formParam("puntosNecesarios")));
        this.ofertaRepository.guardar(oferta);
        this.rubroRepository.guardar(rubro);

        context.redirect("/puntos");


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
