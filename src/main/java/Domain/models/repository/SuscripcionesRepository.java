package Domain.models.repository;

import Domain.models.entities.personaVulnerable.PersonaVulnerable;
import Domain.models.suscripciones.IObservable;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
public class SuscripcionesRepository implements WithSimplePersistenceUnit{

    public Optional<IObservable> buscar(Long id) {

        return Optional.ofNullable(entityManager().find(IObservable.class, id));

    }

    public void guardar(IObservable suscripcion) {
        entityManager().persist(suscripcion);
    }

    public void actualizar(IObservable suscripcion) {
        withTransaction(() -> {
            entityManager().merge(suscripcion);//UPDATE
        });
    }public void eliminar(IObservable suscripcion) {
        entityManager().remove(suscripcion);//DELETE
    }
    @SuppressWarnings("unchecked")
    public Set<IObservable> buscarTodos() {
        return (Set<IObservable>) entityManager()
                .createQuery("from " + IObservable.class.getName())
                .getResultList();
    }

}


