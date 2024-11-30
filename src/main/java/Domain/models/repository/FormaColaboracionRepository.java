package Domain.models.repository;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.formasDeColaborar.FormaDeColaboracion;
import Domain.models.entities.tecnico.Tecnico;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FormaColaboracionRepository implements WithSimplePersistenceUnit {
    public Optional<FormaDeColaboracion> buscar(Long id) {

        return Optional.ofNullable(entityManager().find(FormaDeColaboracion.class, id));


    }
    @SuppressWarnings("unchecked")

    public void guardar(FormaDeColaboracion forma) {
        entityManager().persist(forma);
    }


    public void actualizar(FormaDeColaboracion forma) {
        withTransaction(() -> {
            entityManager().merge(forma);//UPDATE
        });
    } public void eliminar(FormaDeColaboracion forma) {
        EntityManager em = entityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin(); // Iniciar transacción

            if (!em.contains(forma)) {
                forma = em.merge(forma); // Asociar al contexto si está "detached"
            }

            em.remove(forma); // Eliminar la entidad

            transaction.commit(); // Confirmar la transacción
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // Revertir la transacción en caso de error
            }
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<FormaDeColaboracion > buscarTodos() {
        return (List<FormaDeColaboracion>) entityManager()
                .createQuery("from " + FormaDeColaboracion.class)
                .getResultList();
    }




}
