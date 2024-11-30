package Domain.controller.HeladeraController;

import Domain.models.entities.Ubicacion.Geodecodificacion;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.*;
import Domain.models.entities.notificacion.NotificarEmail;
import Domain.models.entities.tecnico.Tecnico;
import Domain.models.repository.HeladeraRepository;
import Domain.models.repository.IncidenteRepository;
import Domain.models.repository.TecnicoRepository;
import Domain.utils.ICrudViewsHandler;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReceptorController implements ICrudViewsHandler, WithSimplePersistenceUnit {
    private IncidenteRepository incidenteRepository = new IncidenteRepository();
    private  HeladeraRepository heladeraRepository = new HeladeraRepository();
    private TecnicoRepository   tecnicoRepository = new TecnicoRepository();
/*
    public ReceptorController() {
        this.incidenteRepository
        this.heladeraRepository = new HeladeraRepository();
    }*/

    @Override
    public void index(Context context) {

    }

    @Override
    public void show(Context context) {

    }

    @Override
    public void create(Context context) {

    }

    @Override
    public void save(Context context) {

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

    public void alarmaTemperatura(Heladera heladera) {
        if(heladera.getEstaActiva()) {
            System.out.println("Estamos");

            heladera.setEstaActiva(false);
            Incidente incidente = new Incidente("Alerta Temperatura", TipoIncidente.ALERTA, heladera, TipoAlerta.TEMPERATURA);
            Tecnico tecnicoSeleccionado = this.tecnicoMasCercano(heladera);

            NotificarEmail notificarEmail = new NotificarEmail();
            notificarEmail.enviarMailTecnico("La heladera " + heladera.getNombreHeladera() + " presenta inconvenientes con el sensor de temperatura. Dirijase lo más pronto posible",tecnicoSeleccionado.getMail());

            try {
                beginTransaction();
                incidenteRepository.guardar(incidente);
                heladeraRepository.actualizar(heladera);
                commitTransaction();
                System.out.println("Incidente guardado y heladera desactivada.");
            } catch (Exception e) {
                rollbackTransaction();
                e.printStackTrace();
                System.out.println("Error al guardar el incidente y desactivar la heladera.");
            }
        }

    }

    public void alarmaMovimiento(Heladera heladera) {
        if(heladera.getEstaActiva()) {
            heladera.setEstaActiva(false);

            Incidente incidente = new Incidente("Alerta Movimiento", TipoIncidente.ALERTA, heladera, TipoAlerta.FRAUDE);
            Tecnico tecnicoSeleccionado = this.tecnicoMasCercano(heladera);

            NotificarEmail notificarEmail = new NotificarEmail();
            notificarEmail.enviarMailTecnico("La heladera " + heladera.getNombreHeladera() + " presenta inconvenientes con el sensor de movimiento. Dirijase lo más pronto posible",tecnicoSeleccionado.getMail());
            try {
                beginTransaction();
                incidenteRepository.guardar(incidente);
                heladeraRepository.actualizar(heladera);
                commitTransaction();
                System.out.println("Incidente guardado y heladera desactivada.");
            } catch (Exception e) {
                rollbackTransaction(); // Asegúrate de tener un método para hacer rollback en caso de error.
                e.printStackTrace();
                System.out.println("Error al guardar el incidente y desactivar la heladera.");
            }
        }
    }

    public void alarmaFallaDesconexion(Heladera heladera) {
        if(heladera.getEstaActiva()) {
            heladera.setEstaActiva(false);

            Incidente incidente = new Incidente("Alerta Desconexion", TipoIncidente.ALERTA, heladera, TipoAlerta.FALLAPORDESCONEXION);
            Tecnico tecnicoSeleccionado = this.tecnicoMasCercano(heladera);

            NotificarEmail notificarEmail = new NotificarEmail();
            notificarEmail.enviarMailTecnico("La heladera " + heladera.getNombreHeladera() + " presenta una falla por desconexión. Dirijase lo más pronto posible",tecnicoSeleccionado.getMail());
            try {
                beginTransaction();
                incidenteRepository.guardar(incidente);
                heladeraRepository.actualizar(heladera);
                commitTransaction();
                System.out.println("Incidente guardado y heladera desactivada.");
            } catch (Exception e) {
                rollbackTransaction();
                e.printStackTrace();
                System.out.println("Error al guardar el incidente y desactivar la heladera.");
            }
        }
    }

    public Tecnico tecnicoMasCercano(Heladera heladera){
        Set<Tecnico> tecnicos = tecnicoRepository.buscarTodos().stream()
                .filter(tecnico -> Geodecodificacion.estaDentroDelRadio(tecnico.getAreaDeCobertura().getPuntoGeografico(), heladera.getUnPuntoGeografico(), tecnico.getAreaDeCobertura().getRadioDeCobertura()))
                .collect(Collectors.toSet());
        return heladera.buscarElTecnicoMasCercano(tecnicos);
    }



}
