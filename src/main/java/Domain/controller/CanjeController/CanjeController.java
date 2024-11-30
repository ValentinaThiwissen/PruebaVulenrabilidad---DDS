package Domain.controller.CanjeController;

import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.formasDeColaborar.Canje;
import Domain.models.entities.formasDeColaborar.Oferta;

import Domain.models.repository.*;
import Domain.utils.ICrudViewsHandler;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.HashMap;

import java.util.Map;
import java.util.Set;

public class CanjeController implements ICrudViewsHandler, WithSimplePersistenceUnit {
    OfertaRepository ofertaRepository;
    CanjeRepository canjeRepository;
    ColaboradorRepository colaboradorRepository;
    UsuarioRepository usuarioRepository;

    public CanjeController(OfertaRepository ofertaRepository, CanjeRepository canjeRepository, ColaboradorRepository colaboradorRepository, UsuarioRepository usuarioRepository){
        this.ofertaRepository = ofertaRepository;
        this.canjeRepository = canjeRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.usuarioRepository = usuarioRepository;

    }

    @Override
    public void index(Context context) {

    }

    @Override
    public void show(Context context) {
        context.render("canjeSuccess.hbs");
    }

    @Override
    public void create(Context context) {
    }

    @Override
    public void save(Context context) {


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

        Canje canje = new Canje();
        canje.setFechaCanje(LocalDate.now());

        if(context.formParam("nombreOferta") == null){
            throw new RuntimeException();
        }
        Double puntos = Double.valueOf(context.formParam("puntos"));
        String nombreOferta = context.formParam("nombreOferta");

        Double puntosAcutales = colaborador.getPuntos();
        if(puntos > puntosAcutales){
            // TODO: si no le alcanzan los puntos que tire un cartelito y lo vuelva llevar a las ofertas
            Map<String, Object> model = new HashMap<>();
            model.put("solicitudExitosa", true);
            model.put("CanjeNoEfectuado", "¡El canje no se puede realizar por falta de puntos!");
            context.redirect("/puntos");

            return;
        }

        colaborador.setPuntos(puntosAcutales - puntos);

        Oferta oferta = ofertaRepository.buscarPorNombre(nombreOferta);
        if(oferta != null){
            canje.setOferta(oferta);
        } else {
            context.status(404).result("Oferta no encontrada");
        }

        canje.setPrecioCanje(puntos);

        colaborador.getCanjesRealizados().add(canje);

        try {
            beginTransaction();
            canjeRepository.guardar(canje);
            colaboradorRepository.actualizar(colaborador);
            ofertaRepository.eliminar(oferta);
            commitTransaction();
            context.redirect("/canjeSuccess");
        } catch (Exception e) {
            e.printStackTrace();
            context.status(500).result("Ha ocurrido un error inesperado.");
        }
    }

    public void createVisualizarCanjes(Context context) {
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

        Set<Canje> canjesRealizados = colaborador.getCanjesRealizados();

        if(canjesRealizados == null){
            context.status(404).result("No hay canjes realizados");

        }
        Map<String, Object> model = new HashMap<>();
        model.put("canjes", canjesRealizados);

        context.render("visualizarCanjes.hbs", model);
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
