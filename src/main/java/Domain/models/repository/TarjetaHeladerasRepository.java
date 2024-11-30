package Domain.models.repository;

import Domain.models.entities.tarjeta.TarjetaDeHeladeras;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TarjetaHeladerasRepository  implements WithSimplePersistenceUnit {

    public Optional<TarjetaDeHeladeras> buscar(Long id) {

        return Optional.ofNullable(entityManager().find(TarjetaDeHeladeras.class, id));


    }
    @SuppressWarnings("unchecked")
    public List<TarjetaDeHeladeras> buscarPorNombre(String nombre) {
        return entityManager()
                .createQuery("from " + TarjetaDeHeladeras.class.getName() + " where nombre =:name")
                .setParameter("name", nombre)
                .getResultList();
    }
    public void guardar(TarjetaDeHeladeras tarjeta) {
        entityManager().persist(tarjeta);
    }


    public void actualizar(TarjetaDeHeladeras tarjeta) {
        withTransaction(() -> {
            entityManager().merge(tarjeta);//UPDATE
        });
    }public void eliminar(TarjetaDeHeladeras tarjeta) {
        entityManager().remove(tarjeta);//DELETE
    }
    @SuppressWarnings("unchecked")
    public Set<TarjetaDeHeladeras> buscarTodos() {
        return (Set<TarjetaDeHeladeras>) entityManager()
                .createQuery("from " + TarjetaDeHeladeras.class.getName())
                .getResultList();
    }

}
