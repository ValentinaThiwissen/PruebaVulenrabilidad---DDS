package Domain.models.repository;

import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.tecnico.Tecnico;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ColaboradorRepository  implements WithSimplePersistenceUnit {

    public Colaborador buscar(Long id) {
        return entityManager().find(Colaborador.class, id);
    }

    public Colaborador buscarPorNombre(String nombre) {
        List<Colaborador> resultados = entityManager()
                .createQuery("from " + Colaborador.class.getName() + " where LOWER(nombre) = LOWER(:name)", Colaborador.class)
                .setParameter("name", nombre.trim())
                .getResultList();
        if (resultados.isEmpty()) {
            return null;
        } else {
            return resultados.get(0);
        }
    }

    public Colaborador buscarPorMail(String mail) {
        List<Colaborador> resultados = entityManager()
                .createQuery("from " + Colaborador.class.getName() + " where LOWER(mail) = LOWER(:name)", Colaborador.class)
                .setParameter("name", mail.trim())
                .getResultList();
        if (resultados.isEmpty()) {
            return null;
        } else {
            return resultados.get(0);
        }
    }

    public Colaborador buscarPorUsuario(Usuario usuario) {
        List<Colaborador> resultados = entityManager()
                .createQuery("FROM Colaborador c WHERE c.usuario = :usuario", Colaborador.class)
                .setParameter("usuario", usuario)
                .getResultList();

        if (resultados.isEmpty()) {
            return null;
        } else {
            return resultados.get(0);
        }
    }
    public Colaborador buscarPorId(Long id) {
        List<Colaborador> resultados = entityManager()
                .createQuery("FROM Colaborador c WHERE c.id = :id", Colaborador.class)
                .setParameter("id", id) // Cambiar "usuario" por "id"
                .getResultList();

        return resultados.isEmpty() ? null : resultados.get(0); // Devuelve null si no hay resultados
    }

    public void guardar(Colaborador colaborador) {
        entityManager().persist(colaborador);
    }


    public void actualizar(Colaborador colaborador) {
        withTransaction(() -> {
            entityManager().merge(colaborador);//UPDATE
        });
    }
    public void eliminar(Colaborador colaborador) {
        EntityManager em = entityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin(); // Iniciar transacción

            if (!em.contains(colaborador)) {
                colaborador = em.merge(colaborador); // Asociar al contexto si está "detached"
            }

            em.remove(colaborador); // Eliminar la entidad

            transaction.commit(); // Confirmar la transacción
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // Revertir la transacción en caso de error
            }
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    public List<Colaborador> buscarTodos() {
        return entityManager()
                .createQuery("from " + Colaborador.class.getName())
                .getResultList();
    }


    public Colaborador buscarPorDNI(Integer nroDni) {
        List<Colaborador> resultados = entityManager()
                .createQuery("FROM Colaborador c WHERE c.nroDNI = :nroDNI", Colaborador.class)
                .setParameter("nroDNI", nroDni) // Cambiar "usuario" por "id"
                .getResultList();

        return resultados.isEmpty() ? null : resultados.get(0); // Devuelve null si no hay resultados
    }

    public Set<String> obtenerTodosLosDNIs() {
        List<String> dnis = entityManager()
                .createQuery("SELECT c.nroDni FROM Colaborador c", String.class).getResultList();
        return new HashSet<>(dnis);
    }
}

