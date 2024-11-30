package Domain.models.repository;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.tecnico.Tecnico;
import Domain.models.entities.tecnico.VisitaTecnica;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class VisitasTecnicasRepository  implements WithSimplePersistenceUnit {

    public Optional<VisitaTecnica> buscar(Long id) {

        return Optional.ofNullable(entityManager().find(VisitaTecnica.class, id));


    }
    @SuppressWarnings("unchecked")
    public List<VisitaTecnica> buscarPorNombre(String nombre) {
        return entityManager()
                .createQuery("from " + VisitaTecnica.class.getName() + " where nombre =:name")
                .setParameter("name", nombre)
                .getResultList();
    }
    public void guardar(VisitaTecnica visitaTecnica) {
        entityManager().persist(visitaTecnica);
    }


    public void actualizar(VisitaTecnica visitaTecnica) {
        withTransaction(() -> {
            entityManager().merge(visitaTecnica);//UPDATE
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
    public List<VisitaTecnica > buscarTodos() {
        return (List<VisitaTecnica >) entityManager()
                .createQuery("from " + VisitaTecnica .class.getName())
                .getResultList();
    }

}
