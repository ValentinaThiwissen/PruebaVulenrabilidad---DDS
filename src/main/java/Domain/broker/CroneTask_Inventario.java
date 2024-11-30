package Domain.broker;
import Domain.controller.HeladeraController.ReceptorController;
import Domain.models.entities.heladera.Heladera;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;


public class CroneTask_Inventario implements Job {
    InventoryService inventoryService = InventoryService.getInstance();
    ReceptorController receptorController = new ReceptorController();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<Heladera> heladerasActivas = inventoryService.getHeladerasActivas();
        for (Heladera heladera : heladerasActivas) {
            if (heladera.pasaronMasDe5Mins()) {
                System.out.println("Heladera " + heladera.getId() + " no recibió publicaciones en los últimos 5 minutos.");
                receptorController.alarmaFallaDesconexion(heladera);
                // Realizar las acciones necesarias si no recibió publicaciones
            }
        }
    }
}



