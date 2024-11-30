package Domain.models.repository;

import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.formasDeColaborar.Oferta;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.Incidente;
import Domain.models.entities.tecnico.Tecnico;
import Domain.utils.ICrudViewsHandler;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class OfertaRepository implements WithSimplePersistenceUnit {
    public Optional<Oferta> buscar(Long id) {

        return Optional.ofNullable(entityManager().find(Oferta.class, id));
    }

    public void guardar(Oferta oferta) {
        entityManager().persist(oferta);
    }
    @SuppressWarnings("unchecked")
    public Oferta buscarPorNombre(String nombre) {
        List<Oferta> resultados = entityManager()
                .createQuery("from " + Oferta.class.getName() + " where LOWER(nombre) = LOWER(:name)", Oferta.class)
                .setParameter("name", nombre.trim())
                .getResultList();

        if (resultados.isEmpty()) {
            return null;
        } else {
            // Retornamos el primer resultado de la lista
            return resultados.get(0);
        }
    }


    public Oferta buscarPorID(Long id) {
        return entityManager().find(Oferta.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Oferta> buscarTodos() {
        return (List<Oferta>) entityManager()
                .createQuery("from " + Oferta.class.getName())
                .getResultList();
    }

    public void actualizar(Oferta oferta) {
        withTransaction(() -> {
            entityManager().merge(oferta);//UPDATE
        });
    }


    public void eliminar(Oferta oferta) {
        EntityManager em = entityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin(); // Iniciar transacción

            if (!em.contains(oferta)) {
                oferta = em.merge(oferta); // Asociar al contexto si está "detached"
            }

            em.remove(oferta); // Eliminar la entidad

            transaction.commit(); // Confirmar la transacción
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // Revertir la transacción en caso de error
            }
            e.printStackTrace();
        }
    }

    public void actualizar2(Oferta oferta) {
        withTransaction(() -> {
            if (oferta.getId() == null) {
                entityManager().persist(oferta); // INSERT
            } else {
                entityManager().merge(oferta); // UPDATE
            }
        });
    }


}
