package Domain.models.repository;

import Domain.models.entities.personaVulnerable.PersonaVulnerable;
import Domain.models.suscripciones.IObservable;
import Domain.models.suscripciones.ReubicarViandas;
import Domain.models.suscripciones.Suscripcion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
public class SuscripcionReubicarRepository implements WithSimplePersistenceUnit{

    public ReubicarViandas buscar(Long id) {

        return entityManager().find(ReubicarViandas.class, id);

    }

    public void guardar(ReubicarViandas suscripcion) {
        entityManager().persist(suscripcion);
    }

    public void actualizar(ReubicarViandas suscripcion) {
        withTransaction(() -> {
            entityManager().merge(suscripcion);//UPDATE
        });
    }public void eliminar(ReubicarViandas suscripcion) {
        entityManager().remove(suscripcion);//DELETE
    }
    @SuppressWarnings("unchecked")
    public Set<ReubicarViandas> buscarTodos() {
        return (Set<ReubicarViandas>) entityManager()
                .createQuery("from " + ReubicarViandas.class.getName())
                .getResultList();
    }

}


