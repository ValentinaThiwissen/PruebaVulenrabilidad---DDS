package Domain.models.repository;

import Domain.models.entities.formasDeColaborar.Oferta;
import Domain.models.entities.formasDeColaborar.Producto;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class ProductoRepository implements WithSimplePersistenceUnit {

    public Optional<Producto> buscar(Long id) {

        return Optional.ofNullable(entityManager().find(Producto.class, id));
    }

    public void guardar(Producto producto) {
        entityManager().persist(producto);
    }
    @SuppressWarnings("unchecked")



    public Oferta buscarPorID(Long id) {
        return entityManager().find(Oferta.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Producto> buscarTodos() {
        return (List<Producto>) entityManager()
                .createQuery("from " + Producto.class.getName())
                .getResultList();
    }

    public void actualizar(Producto producto) {
        withTransaction(() -> {
            entityManager().merge(producto);//UPDATE
        });
    }


    public void eliminar(Producto producto) {
        EntityManager em = entityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin(); // Iniciar transacción

            if (!em.contains(producto)) {
                producto = em.merge(producto); // Asociar al contexto si está "detached"
            }

            em.remove(producto); // Eliminar la entidad

            transaction.commit(); // Confirmar la transacción
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // Revertir la transacción en caso de error
            }
            e.printStackTrace();
        }
    }




}
