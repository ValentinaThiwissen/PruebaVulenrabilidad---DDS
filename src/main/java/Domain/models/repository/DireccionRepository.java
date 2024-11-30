package Domain.models.repository;

import Domain.models.entities.Ubicacion.Direccion;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.formasDeColaborar.Canje;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class DireccionRepository implements WithSimplePersistenceUnit {
    public Optional<Direccion> buscar(Long id) {

        return Optional.ofNullable(entityManager().find(Direccion.class, id));
    }

    public void guardar(Direccion direccion) {
        entityManager().persist(direccion);
    }
    @SuppressWarnings("unchecked")



    public Direccion buscarPorID(Long id) {
        return entityManager().find(Direccion.class, id);
    }

    @SuppressWarnings("unchecked")
    public Set<Direccion> buscarTodos() {
        return (Set<Direccion>) entityManager()
                .createQuery("from " + Direccion.class.getName())
                .getResultList();
    }
    List<Direccion> buscarPorNombre(String nombre) {
        return entityManager()
                .createQuery("from " + Direccion.class.getName() + " where nombre =:name")
                .setParameter("name", nombre)
                .getResultList();
    }

    public void actualizar(Direccion direccion) {
        withTransaction(() -> {
            entityManager().merge(direccion);//UPDATE
        });
    }


    public void eliminar(Direccion direccion) {
        entityManager().remove(direccion);//DELETE
    }
}
