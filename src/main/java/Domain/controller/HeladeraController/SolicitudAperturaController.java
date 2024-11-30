package Domain.controller.HeladeraController;

import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.Incidente;
import Domain.models.entities.heladera.MotivoMovimiento;
import Domain.models.entities.heladera.TipoIncidente;
import Domain.models.entities.personaVulnerable.TipoDocumento;
import Domain.models.entities.tarjeta.SolicitudApertura;
import Domain.models.repository.ColaboradorRepository;
import Domain.models.repository.HeladeraRepository;
import Domain.models.repository.SolicitudAperturaRepository;
import Domain.models.repository.UsuarioRepository;
import Domain.utils.ICrudViewsHandler;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SolicitudAperturaController implements ICrudViewsHandler, WithSimplePersistenceUnit {

    SolicitudAperturaRepository solicitudAperturaRepository;
    HeladeraRepository heladeraRepository;
    UsuarioRepository usuarioRepository;
    ColaboradorRepository colaboradorRepository;
    private static final Logger logger = Logger.getLogger(IncidenteController.class.getName());
    public SolicitudAperturaController(SolicitudAperturaRepository solicitudAperturaRepository, HeladeraRepository heladeraRepository, UsuarioRepository usuarioRepository, ColaboradorRepository colaboradorRepository){
        this.solicitudAperturaRepository= solicitudAperturaRepository;
        this.heladeraRepository= heladeraRepository;
        this.usuarioRepository = usuarioRepository;
        this.colaboradorRepository = colaboradorRepository;

    }
    @Override
    public void index(Context context) {
        List<Heladera> heladeras = this.heladeraRepository.buscarTodos();
        Map<String, Object> model = new HashMap<>();
        model.put("heladeras", heladeras);
        context.render("registroApertura.hbs", model);
    }

    @Override
    public void show(Context context) {

    }

    @Override
    public void create(Context context) {

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

        if (colaborador == null) {
            context.status(404).result("Colaborador no encontrado");
            return;
        }

        Long idHeladera = Long.valueOf(context.formParam("heladera"));
        Heladera heladera = heladeraRepository.buscarPorID(idHeladera);


        if(heladera == null){
            context.status(213).result("Error no existe la heladera: " + idHeladera);
            logger.info("Se guardo la solicitud de apertura");
        }else{

            SolicitudApertura solicitudApertura = new SolicitudApertura();

            solicitudApertura.setTarjetaSolicitante(colaborador.getTarjetaDistribucionViandas());
            solicitudApertura.setFechaDeSolicitud(LocalDate.now());
            solicitudApertura.setHoraSolicitudTarjeta(LocalDateTime.now());

            Integer cantidadViandas = Integer.valueOf(context.formParam("cantidadViandas"));
            solicitudApertura.setViandas(cantidadViandas);

            String motivoSeleccionado = context.formParam("motivo");
            Map<String, Object> model = new HashMap<>();
            // Verificar que el motivo es válido dentro de la enumeración
            try {
                solicitudApertura.setHeladeraUtilizada(heladera);
                if (motivoSeleccionado.equalsIgnoreCase("ingresarViandas")) {
                    solicitudApertura.setMotivo(MotivoMovimiento.INGRESARVIANDA);
                } else if (motivoSeleccionado.equalsIgnoreCase("retirarViandas")) {
                    solicitudApertura.setMotivo(MotivoMovimiento.RETIRARVIANDA);
                }
                heladera.getSolicitudesDeApertura().add(solicitudApertura);

                // Guardar la solicitud
                beginTransaction();
                this.solicitudAperturaRepository.guardar(solicitudApertura);
                logger.info("Se guardo la solicitud de apertura");
                this.heladeraRepository.actualizar(heladera);
                logger.info("Se actualizo la heladera ");
                commitTransaction();
                //context.status(200).result("Solicitud de apertura registrada con éxito.");
                model.put("solicitudExitosa", "¡La solicitud de apertura fue enviada con éxito!");
                context.render("registroApertura.hbs", model);

            } catch (IllegalArgumentException e) {
                context.status(400).result("Motivo no válido: " + motivoSeleccionado);
            }
        }
    }

}



