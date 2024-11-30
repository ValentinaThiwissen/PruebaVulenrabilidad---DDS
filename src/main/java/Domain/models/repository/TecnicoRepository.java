package Domain.models.repository;

import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.tecnico.Tecnico;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TecnicoRepository  implements WithSimplePersistenceUnit {

    public Optional<Tecnico> buscar(Long id) {

        return Optional.ofNullable(entityManager().find(Tecnico.class, id));


    }

    @SuppressWarnings("unchecked")
    public List<Tecnico> buscarPorNombre(String nombre) {
        return entityManager()
                .createQuery("from " + Tecnico.class.getName() + " where nombre =:name")
                .setParameter("name", nombre)
                .getResultList();
    }
    public Tecnico buscarPorUsuario(Usuario usuario) {
        List<Tecnico> resultados = entityManager()
                .createQuery("FROM Tecnico c WHERE c.usuario = :usuario", Tecnico.class)
                .setParameter("usuario", usuario)
                .getResultList();

        if (resultados.isEmpty()) {
            return null;
        } else {
            return resultados.get(0);
        }
    }

    public void guardar(Tecnico tecnico) {
        entityManager().persist(tecnico);
    }


    public void actualizar(Tecnico tecnico) {
        withTransaction(() -> {
            entityManager().merge(tecnico);//UPDATE
        });
    }

    @SuppressWarnings("unchecked")

    public Set<Tecnico> buscarTodos() {
        return new HashSet<>(entityManager()
                .createQuery("from " + Tecnico.class.getName(), Tecnico.class)
                .getResultList());
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
