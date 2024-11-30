package Domain.models.repository;

import Domain.models.entities.formasDeColaborar.Oferta;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.Incidente;
import Domain.models.entities.heladera.Modelo;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.springframework.boot.Banner;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class ModeloRepository implements WithSimplePersistenceUnit {
    public Optional<Modelo> buscar(Long id) {

        return Optional.ofNullable(entityManager().find(Modelo.class, id));
    }

    public void guardar(Modelo modelo) {
        entityManager().persist(modelo);
    }
    @SuppressWarnings("unchecked")
    public Modelo buscarPorNombre(String nombre) {
        List<Modelo> resultados = entityManager()
                .createQuery("from " + Modelo.class.getName() + " where nombreModelo =:name")
                .setParameter("name", nombre)
                .getResultList();
        if (resultados.isEmpty()) {
            return null;
        } else {
            return resultados.get(0);
        }
    }




    public Modelo buscarPorID(Long id) {
        return entityManager().find(Modelo.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Modelo> buscarTodos() {
        return (List<Modelo>) entityManager()
                .createQuery("from " + Modelo.class.getName())
                .getResultList();
    }

    public void actualizar(Modelo modelo) {
        withTransaction(() -> {
            entityManager().merge(modelo);//UPDATE
        });
    }


    public void eliminar(Modelo modelo) {
        EntityManager em = entityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin(); // Iniciar transacción

            if (!em.contains(modelo)) {
                modelo = em.merge(modelo); // Asociar al contexto si está "detached"
            }

            em.remove(modelo); // Eliminar la entidad

            transaction.commit(); // Confirmar la transacción
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // Revertir la transacción en caso de error
            }
            e.printStackTrace();
        }
    }

    public void actualizar2(Modelo modelo) {
        withTransaction(() -> {
            if (modelo.getId() == null) {
                entityManager().persist(modelo); // INSERT
            } else {
                entityManager().merge(modelo); // UPDATE
            }
        });
    }
}
