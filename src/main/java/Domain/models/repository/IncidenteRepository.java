package Domain.models.repository;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.Incidente;
import Domain.models.entities.tecnico.Tecnico;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class IncidenteRepository  implements WithSimplePersistenceUnit {

    public Optional<Incidente> buscar(Long id) {

        return Optional.ofNullable(entityManager().find(Incidente.class, id));
    }

    public void guardar(Incidente incidente) {
        entityManager().persist(incidente);
    }

    public List<Incidente> buscarPorHeladeraId(Long heladeraId) {
        return entityManager()
                .createQuery("from Incidente i where i.heladeraIncidente.id = :heladeraId", Incidente.class)
                .setParameter("heladeraId", heladeraId)
                .getResultList();
    }

    public void actualizar(Incidente incidente) {
        withTransaction(() -> {
            entityManager().merge(incidente);//UPDATE
        });
    } public void eliminar(Tecnico tecnico) {
        EntityManager em = entityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin(); // Iniciar transacción

            if (!em.contains(tecnico)) {
                tecnico = em.merge(tecnico); // Asociar al contexto si está "detached"
            }

            em.remove(tecnico); // Eliminar la entidad

            transaction.commit(); // Confirmar la transacción
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // Revertir la transacción en caso de error
            }
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    public List<Incidente> buscarTodos() {
        return (List<Incidente>) entityManager()
                .createQuery("from " + Incidente.class.getName())
                .getResultList();
    }

}
