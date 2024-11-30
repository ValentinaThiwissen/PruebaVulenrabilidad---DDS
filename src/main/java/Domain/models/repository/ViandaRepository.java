package Domain.models.repository;


import Domain.models.entities.tecnico.Tecnico;
import Domain.models.entities.vianda.Vianda;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ViandaRepository implements WithSimplePersistenceUnit {
    public Optional<Vianda> buscar(Long id) {
        return Optional.ofNullable(entityManager().find(Vianda.class, id));
    }

    public void guardar(Vianda vianda) {
        entityManager().persist(vianda);
    }
    @SuppressWarnings("unchecked")
    public List<Vianda> buscarPorNombre(String nombre) {
        return entityManager()
                .createQuery("from " + Vianda.class.getName() + " where nombre =:name")
                .setParameter("name", nombre)
                .getResultList();
    }


    public Vianda buscarPorID(Long id) {
        return entityManager().find(Vianda.class, id);
    }

    @SuppressWarnings("unchecked")
    public Set<Vianda> buscarTodos() {
        return (Set<Vianda>) entityManager()
                .createQuery("from " + Vianda.class.getName())
                .getResultList();
    }

    public void actualizar(Vianda vianda) {
        withTransaction(() -> {
            entityManager().merge(vianda);//UPDATE
        });
    }


    public void eliminar(Tecnico tecnico) {
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
}
