package Domain.models.repository;

import Domain.models.entities.formasDeColaborar.Rubro;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.Optional;
import java.util.Set;

public class RubroRepository implements WithSimplePersistenceUnit {
    public Optional<Rubro> buscar(Long id) {

        return Optional.ofNullable(entityManager().find(Rubro.class, id));
    }

    public void guardar(Rubro rubro) {
        entityManager().persist(rubro);
    }
    @SuppressWarnings("unchecked")



    public Rubro buscarPorID(Long id) {
        return entityManager().find(Rubro.class, id);
    }

    @SuppressWarnings("unchecked")
    public Set<Rubro> buscarTodos() {
        return (Set<Rubro>) entityManager()
                .createQuery("from " + Rubro.class.getName())
                .getResultList();
    }

    public void actualizar(Rubro rubro) {
        withTransaction(() -> {
            entityManager().merge(rubro);//UPDATE
        });
    }


    public void eliminar(Rubro rubro) {
        entityManager().remove(rubro);//DELETE
    }




}
