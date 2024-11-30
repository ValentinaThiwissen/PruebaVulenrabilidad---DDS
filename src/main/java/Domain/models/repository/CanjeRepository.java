package Domain.models.repository;

import Domain.models.entities.formasDeColaborar.Canje;
import Domain.models.entities.heladera.Heladera;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CanjeRepository implements WithSimplePersistenceUnit {
    public Optional<Canje> buscar(Long id) {return Optional.ofNullable(entityManager().find(Canje.class, id));
    }

    public void guardar(Canje canje) {
        entityManager().persist(canje);
    }

    public Canje buscarPorID(Long id) {
        return entityManager().find(Canje.class, id);
    }

    public Set<Canje> buscarTodos() {
        List<Canje> canjesList = entityManager()
                .createQuery("from " + Canje.class.getName(), Canje.class)
                .getResultList();
        return new HashSet<>(canjesList);
    }

    public void actualizar(Canje canje) {
        withTransaction(() -> {
            entityManager().merge(canje);
        });
    }

    public void eliminar(Canje canje) {
        entityManager().remove(canje);//DELETE
    }
}
