package Domain.models.repository;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.tarjeta.TarjetaDistribucionViandas;
import Domain.models.entities.tecnico.Tecnico;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TarjetaDistribucionViandaRepository implements WithSimplePersistenceUnit {
    public TarjetaDistribucionViandas buscar(Long id) {
        return entityManager().find(TarjetaDistribucionViandas.class, id);
    }

    public void guardar(TarjetaDistribucionViandas tarjeta) {
        entityManager().persist(tarjeta);
    }


    public void actualizar(TarjetaDistribucionViandas tarjeta) {
        withTransaction(() -> {
            entityManager().merge(tarjeta);//UPDATE
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
    public Set<Colaborador> buscarTodos() {
        return (Set<Colaborador>) entityManager()
                .createQuery("from " + TarjetaDistribucionViandas.class.getName())
                .getResultList();
    }

}




