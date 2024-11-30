package Domain.config;

import Domain.controller.AdministrarController;
import Domain.controller.CanjeController.CanjeController;
import Domain.controller.CanjeController.ProductosController;
import Domain.controller.ColaboracionController.FormaColaboracionHController;
import Domain.controller.ColaboracionController.FormaColaboracionJController;
import Domain.controller.ColaboradorController.ColaboradorHumanoController;
import Domain.controller.ColaboradorController.ColaboradorJuridicoController;
import Domain.controller.HeladeraController.HeladeraController;
import Domain.controller.HeladeraController.IncidenteController;
import Domain.controller.HeladeraController.SolicitudAperturaController;
import Domain.controller.LogInController.LogInController;
import Domain.controller.MigradorController.MigradorController;
import Domain.controller.PersonaVulnerableController.PersonaVulnerableController;
import Domain.controller.ReporteController;
import Domain.controller.SuscripcionColaboradorController.SuscripcionColaboradorController;
import Domain.controller.TecnicoController.TecnicoController;
import Domain.controller.UsuarioController.MiPerfilController;
import Domain.controller.UsuarioController.UsuarioController;
import Domain.models.entities.Usuario.Rol;
import Domain.models.entities.validadorcontrasenia.ValidadorContrasenea;
import Domain.models.repository.*;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private static Map<String, Object> instances = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T instanceOf(Class<T> componentClass) {
        String componentName = componentClass.getName();
        if (!instances.containsKey(componentName)) {
            if (componentName.equals(ProductosController.class.getName())) {
                ProductosController instance = new ProductosController(instanceOf(OfertaRepository.class), instanceOf(RubroRepository.class), instanceOf(UsuarioRepository.class), instanceOf(ColaboradorRepository.class));
                instances.put(componentName, instance);
            } else if (componentName.equals(OfertaRepository.class.getName())) {
                OfertaRepository instance = new OfertaRepository();
                instances.put(componentName, instance);
            } else if (componentName.equals(RubroRepository.class.getName())) {
                RubroRepository instance = new RubroRepository();
                instances.put(componentName, instance);
            } else if (componentName.equals(UsuarioRepository.class.getName())) {
                UsuarioRepository instance = new UsuarioRepository();
                instances.put(componentName, instance);
            } else if (componentName.equals(RolRepository.class.getName())) {
                RolRepository instance = new RolRepository();
                instances.put(componentName, instance);
            } else if (componentName.equals(IncidenteController.class.getName())) {
                IncidenteController instance = new IncidenteController(instanceOf(IncidenteRepository.class), instanceOf(HeladeraRepository.class), instanceOf(UsuarioRepository.class), instanceOf(RolRepository.class),instanceOf(ColaboradorRepository.class));
                instances.put(componentName, instance);
            } else if (componentName.equals(IncidenteRepository.class.getName())) {
                IncidenteRepository instance = new IncidenteRepository();
                instances.put(componentName, instance);
            } else if (componentName.equals(HeladeraController.class.getName())) {
                HeladeraController instance = new HeladeraController(
                        instanceOf(HeladeraRepository.class));
                instances.put(componentName, instance);
            } else if (componentName.equals(HeladeraRepository.class.getName())) {
                HeladeraRepository instance = new HeladeraRepository();
                instances.put(componentName, instance);
            } else if (componentName.equals(SolicitudAperturaController.class.getName())) {
                SolicitudAperturaController instance = new SolicitudAperturaController(
                        instanceOf(SolicitudAperturaRepository.class), instanceOf(HeladeraRepository.class), instanceOf(UsuarioRepository.class), instanceOf(ColaboradorRepository.class));
                instances.put(componentName, instance);
            } else if (componentName.equals(PersonaVulnerableController.class.getName())) {
                PersonaVulnerableController instance = new PersonaVulnerableController(instanceOf(PersonaVulnerableRepository.class), instanceOf(UsuarioRepository.class), instanceOf(RolRepository.class));
                instances.put(componentName, instance);
            } else if (componentName.equals(PersonaVulnerableRepository.class.getName())) {
                PersonaVulnerableRepository instance = new PersonaVulnerableRepository();
                instances.put(componentName, instance);
            } else if (componentName.equals(ColaboradorJuridicoController.class.getName())) {
                ColaboradorJuridicoController instance = new ColaboradorJuridicoController(instanceOf(ColaboradorRepository.class), instanceOf(UsuarioRepository.class));
                instances.put(componentName, instance);
            } else if (componentName.equals(ColaboradorHumanoController.class.getName())) {
                ColaboradorHumanoController instance = new ColaboradorHumanoController(instanceOf(ColaboradorRepository.class), instanceOf(UsuarioRepository.class), instanceOf(RolRepository.class), instanceOf(TarjetaHeladerasRepository.class), instanceOf(TarjetaDistribucionViandaRepository.class));
                instances.put(componentName, instance);
            } else if (componentName.equals(ColaboradorRepository.class.getName())) {
                ColaboradorRepository instance = new ColaboradorRepository();
                instances.put(componentName, instance);
            } else if (componentName.equals(LogInController.class.getName())) {
                LogInController instance = new LogInController(instanceOf(UsuarioRepository.class), instanceOf(ColaboradorRepository.class), instanceOf(ValidadorContrasenea.class));
                instances.put(componentName, instance);
            } else if (componentName.equals(SuscripcionReubicarRepository.class.getName())) {
                SuscripcionReubicarRepository instance = new SuscripcionReubicarRepository();
                instances.put(componentName, instance);
            }else if (componentName.equals(SuscripcionColaboradorController.class.getName())) {
                SuscripcionColaboradorController instance = new SuscripcionColaboradorController(instanceOf(SuscripcionesRepository.class), instanceOf(HeladeraRepository.class), instanceOf(UsuarioRepository.class), instanceOf(ColaboradorRepository.class), instanceOf(SuscripcionReubicarRepository.class));
                instances.put(componentName, instance);
            } else if (componentName.equals(SuscripcionesRepository.class.getName())) {
                SuscripcionesRepository instance = new SuscripcionesRepository();
                instances.put(componentName, instance);
            } else if (componentName.equals(MiPerfilController.class.getName())) {
                MiPerfilController instance = new MiPerfilController(instanceOf(ColaboradorRepository.class), instanceOf(UsuarioRepository.class));
                instances.put(componentName, instance);
            } else if (componentName.equals(FormaColaboracionHController.class.getName())) {
                FormaColaboracionHController instance = new FormaColaboracionHController(instanceOf(FormaColaboracionRepository.class), instanceOf(HeladeraRepository.class), instanceOf(UsuarioRepository.class), instanceOf(PersonaVulnerableRepository.class), instanceOf(OfertaRepository.class), instanceOf(ColaboradorRepository.class), instanceOf(ViandaRepository.class), instanceOf(TarjetaHeladerasRepository.class), instanceOf(TarjetaDistribucionViandaRepository.class), instanceOf(ModeloRepository.class), instanceOf(TecnicoRepository.class));
                instances.put(componentName, instance);
            } else if (componentName.equals(SolicitudAperturaRepository.class.getName())) {
                SolicitudAperturaRepository instance = new SolicitudAperturaRepository();
                instances.put(componentName, instance);
            } else if (componentName.equals(FormaColaboracionRepository.class.getName())) {
                FormaColaboracionRepository instance = new FormaColaboracionRepository();
                instances.put(componentName, instance);
            } else if (componentName.equals(UsuarioController.class.getName())) {
                UsuarioController instance = new UsuarioController(instanceOf(UsuarioRepository.class), instanceOf(ColaboradorRepository.class), instanceOf(ValidadorContrasenea.class),instanceOf(RolRepository.class));
                instances.put(componentName, instance);
            } else if(componentName.equals(ViandaRepository.class.getName())){
                ViandaRepository instance = new ViandaRepository();
                instances.put(componentName, instance);
            } else if(componentName.equals(TarjetaHeladerasRepository.class.getName())){
                TarjetaHeladerasRepository instance = new TarjetaHeladerasRepository();
                instances.put(componentName, instance);
            } else if (componentName.equals(MigradorController.class.getName())) {
                MigradorController instance = new MigradorController(instanceOf(ColaboradorRepository.class));
                instances.put(componentName, instance);
            } else if(componentName.equals(TarjetaDistribucionViandaRepository.class.getName())){
                TarjetaDistribucionViandaRepository instance = new TarjetaDistribucionViandaRepository();
                instances.put(componentName,instance);
            } else if(componentName.equals(ModeloRepository.class.getName())){
                ModeloRepository instance = new ModeloRepository();
                instances.put(componentName,instance);
            } else if(componentName.equals(AdministrarController.class.getName())){
                AdministrarController instance = new AdministrarController(instanceOf(UsuarioRepository.class), instanceOf(RolRepository.class), instanceOf(IncidenteRepository.class), instanceOf(OfertaRepository.class), instanceOf(ColaboradorRepository.class),instanceOf(FormularioRepository.class),instanceOf(VisitasTecnicasRepository.class));
                instances.put(componentName,instance);
            } else if (componentName.equals(FormaColaboracionJController.class.getName())) {
                FormaColaboracionJController instance = new FormaColaboracionJController(instanceOf(UsuarioRepository.class), instanceOf(ColaboradorRepository.class),instanceOf(OfertaRepository.class),instanceOf(FormaColaboracionRepository.class),instanceOf(HeladeraRepository.class));
                instances.put(componentName,instance);
            } else if(componentName.equals(CanjeController.class.getName())){
                CanjeController instance = new CanjeController(instanceOf(OfertaRepository.class), instanceOf(CanjeRepository.class), instanceOf(ColaboradorRepository.class), instanceOf(UsuarioRepository.class));
                instances.put(componentName,instance);
            } else if(componentName.equals(CanjeRepository.class.getName())){
                CanjeRepository instance = new CanjeRepository();
                instances.put(componentName,instance);
            } else if(componentName.equals(FormularioRepository.class.getName())){
                FormularioRepository instance = new FormularioRepository();
                instances.put(componentName,instance);
            } else if(componentName.equals(TecnicoController.class.getName())){
                TecnicoController instance = new TecnicoController(instanceOf(VisitasTecnicasRepository.class),instanceOf(HeladeraRepository.class),instanceOf(UsuarioRepository.class),instanceOf(RolRepository.class), instanceOf(TecnicoRepository.class));
                instances.put(componentName,instance);
            } else if(componentName.equals(VisitasTecnicasRepository.class.getName())){
                VisitasTecnicasRepository instance = new VisitasTecnicasRepository();
                instances.put(componentName,instance);
            } else if (componentName.equals(TecnicoRepository.class.getName())){
                TecnicoRepository instance = new TecnicoRepository();
                instances.put(componentName,instance);
            } else if (componentName.equals(ReporteController.class.getName())){
                ReporteController instance = new ReporteController(instanceOf(ReporteRepository.class), instanceOf(UsuarioRepository.class));
                instances.put(componentName,instance);
            } else if (componentName.equals(ReporteRepository.class.getName())){
                ReporteRepository instance = new ReporteRepository();
                instances.put(componentName,instance);
            }
        }
        return (T) instances.get(componentName);
    }
}


