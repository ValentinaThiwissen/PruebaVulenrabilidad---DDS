package Domain.controller.SuscripcionColaboradorController;

import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.colaborador.MedioDeContacto;
import Domain.models.entities.colaborador.TipoDeContacto;
import Domain.models.entities.formasDeColaborar.MotivoDistribucion;
import Domain.models.entities.formasDeColaborar.Oferta;
import Domain.models.entities.heladera.Heladera;
import Domain.models.repository.*;
import Domain.models.suscripciones.*;
import Domain.utils.ICrudViewsHandler;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.*;
import java.util.stream.Collectors;


public class SuscripcionColaboradorController implements ICrudViewsHandler , WithSimplePersistenceUnit {
    SuscripcionesRepository suscripcionesRepository;
    HeladeraRepository heladeraRepository;
    UsuarioRepository usuarioRepository;
    ColaboradorRepository colaboradorRepository;
    SuscripcionReubicarRepository suscripcionReubicarRepository;

    public SuscripcionColaboradorController(SuscripcionesRepository suscripcionesRepository, HeladeraRepository heladeraRepository, UsuarioRepository usuarioRepository, ColaboradorRepository colaboradorRepository, SuscripcionReubicarRepository suscripcionReubicarRepository) {
        this.suscripcionesRepository = suscripcionesRepository;
        this.heladeraRepository = heladeraRepository;
        this.usuarioRepository = usuarioRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.suscripcionReubicarRepository = suscripcionReubicarRepository;
    }

    @Override
    public void index(Context context) {
        List<Heladera> heladeras = this.heladeraRepository.buscarTodos();
        Map<String, Object> model = new HashMap<>();
        model.put("heladeras", heladeras);
        context.render("suscripcion.hbs", model);

    }

    @Override
    public void show(Context context) {

    }

    @Override
    public void create(Context context) {
        context.render("suscripcion.hbs");
    }

    @Override
    public void save(Context context) {
        try {
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

            Suscripcion suscripcion = new Suscripcion();



            String action = context.formParam("action");
            Map<String, Object> model = new HashMap<>();

            switch (action) {
                case "reducirViandas": {
                    Long idHeladera = Long.valueOf(context.formParam("heladera"));
                    Heladera heladera = heladeraRepository.buscarPorID(idHeladera);

                    if (heladera == null) {
                        context.status(213).result("Error no existe la heladera: " + idHeladera);
                    } else {
                        suscripcion.setHeladeraOrigen(heladera);
                    }
                    ReducirViandas casoSuscripcion = new ReducirViandas();
                    Integer viandasParaQueEsteLLena = Integer.parseInt(context.formParam("viandasParaQueEsteLLena"));
                    casoSuscripcion.setViandasParaQueEsteLlena(viandasParaQueEsteLLena);
                    suscripcion.setMotivo(MotivoDistribucion.SOBRAN_VIANDAS);
                    casoSuscripcion.setSuscripcion(suscripcion);

                    String contacto = context.formParam("contacto");
                    String tipoContacto = context.formParam("tipoContacto");
                    TipoDeContacto tipoDeContacto = TipoDeContacto.valueOf(tipoContacto.toUpperCase());
                    MedioDeContacto medioDeContacto = new MedioDeContacto(contacto, tipoDeContacto);

                    colaborador.setMedioDeNotificacionDeseado(medioDeContacto);

                    casoSuscripcion.agregarObserver(colaborador);

                    beginTransaction();
                    suscripcionesRepository.guardar(casoSuscripcion);
                    colaboradorRepository.actualizar(colaborador);
                    commitTransaction();

                    model.put("solicitudExitosa", true);
                    model.put("reporteExitoso", "¡La suscripción fue enviada con éxito!");
                    context.render("suscripcion.hbs", model);

                    break;
                }
                case "agregarViandas": {
                    Long idHeladera = Long.valueOf(context.formParam("heladera"));
                    Heladera heladera = heladeraRepository.buscarPorID(idHeladera);

                    if (heladera == null) {
                        context.status(213).result("Error no existe la heladera: " + idHeladera);
                    } else {
                        suscripcion.setHeladeraOrigen(heladera);
                    }
                    AgregarViandas casoSuscripcion = new AgregarViandas();
                    Integer viandasDisponibles = Integer.parseInt(context.formParam("viandasDisponibles"));
                    casoSuscripcion.setViandasDisponibles(viandasDisponibles);
                    suscripcion.setMotivo(MotivoDistribucion.FALTA_VIANDAS);
                    casoSuscripcion.setSuscripcion(suscripcion);

                    String contacto = context.formParam("contacto");
                    String tipoContacto = context.formParam("tipoContacto");
                    TipoDeContacto tipoDeContacto = TipoDeContacto.valueOf(tipoContacto.toUpperCase());
                    MedioDeContacto medioDeContacto = new MedioDeContacto(contacto, tipoDeContacto);

                    colaborador.setMedioDeNotificacionDeseado(medioDeContacto);
                    casoSuscripcion.agregarObserver(colaborador);

                    beginTransaction();
                    suscripcionesRepository.guardar(casoSuscripcion);
                    colaboradorRepository.actualizar(colaborador);
                    commitTransaction();


                    model.put("solicitudExitosa", true);
                    model.put("reporteExitoso", "¡La suscripción fue enviada con éxito!");
                    context.render("suscripcion.hbs", model);

                    break;
                }
                case "reubicarViandas":{
                    Long idHeladera = Long.valueOf(context.formParam("heladera"));
                    Heladera heladera = heladeraRepository.buscarPorID(idHeladera);

                    if (heladera == null) {
                        context.status(213).result("Error no existe la heladera: " + idHeladera);
                    } else {
                        suscripcion.setHeladeraOrigen(heladera);
                    }

                    ReubicarViandas casoSuscripcion = new ReubicarViandas();
                    suscripcion.setMotivo(MotivoDistribucion.DESPERFECTO_HELADERA);
                    casoSuscripcion.setSuscripcion(suscripcion);

                    String contacto = context.formParam("contacto");
                    String tipoContacto = context.formParam("tipoContacto");
                    TipoDeContacto tipoDeContacto = TipoDeContacto.valueOf(tipoContacto.toUpperCase());
                    MedioDeContacto medioDeContacto = new MedioDeContacto(contacto, tipoDeContacto);

                    colaborador.setMedioDeNotificacionDeseado(medioDeContacto);
                    casoSuscripcion.agregarObserver(colaborador);

                    try {
                        beginTransaction();
                        //suscripcionesRepository.guardarCanje(casoSuscripcion);
                        suscripcionReubicarRepository.guardar(casoSuscripcion);
                        colaboradorRepository.actualizar(colaborador);
                        commitTransaction();
                        model.put("solicitudExitosa", true);
                        model.put("reporteExitoso", "¡La suscripción fue enviada con éxito!");
                        context.render("suscripcion.hbs", model);

                    }catch (Exception e) {
                        context.status(500).result("Error al guardarCanje "+ e.getMessage() );
                    }



                    break;
                }
                default:
                    context.status(400).result("Acción desconocida");
            }

        } catch (Exception e) {
            context.status(500).result("Error al procesar la solicitud: " + e.getMessage());
        }

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
