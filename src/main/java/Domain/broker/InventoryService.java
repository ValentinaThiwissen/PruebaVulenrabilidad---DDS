package Domain.broker;

import Domain.models.entities.heladera.Heladera;
import Domain.models.repository.HeladeraRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import java.util.ArrayList;

public class InventoryService implements WithSimplePersistenceUnit {
    private static InventoryService instance;
    private List<Heladera> heladeras;
    private HeladeraRepository heladeraRepository = new HeladeraRepository();

    private InventoryService() {
        // Constructor privado para Singleton
        heladeras = new ArrayList<>();
        // Inicializar la lista de heladeras
    }

    public static InventoryService getInstance() {
        if (instance == null) {
            instance = new InventoryService();
        }
        return instance;
    }

    public List<Heladera> getHeladerasActivas() {
        this.heladeras = heladeraRepository.buscarTodos();
        List <Heladera> activas = new ArrayList<>();

        for (Heladera heladera : heladeras) {
            if (heladera.getEstaActiva()) {
                activas.add(heladera);
            }
        }
        return activas;
    }
}

