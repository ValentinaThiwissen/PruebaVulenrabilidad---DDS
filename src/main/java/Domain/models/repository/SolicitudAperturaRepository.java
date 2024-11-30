package Domain.models.repository;

import Domain.models.entities.personaVulnerable.PersonaVulnerable;
import Domain.models.entities.tarjeta.SolicitudApertura;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SolicitudAperturaRepository implements WithSimplePersistenceUnit {
    public Optional<SolicitudApertura> buscar(Long id) {

        return Optional.ofNullable(entityManager().find(SolicitudApertura.class, id));


    }
    @SuppressWarnings("unchecked")

    public void guardar(SolicitudApertura solicitudApertura) {
        entityManager().persist(solicitudApertura);
    }


    public void actualizar(SolicitudApertura solicitudApertura) {
        withTransaction(() -> {
            entityManager().merge(solicitudApertura);//UPDATE
        });
    }public void eliminar(SolicitudApertura solicitudApertura) {
        entityManager().remove(solicitudApertura);//DELETE
    }
    @SuppressWarnings("unchecked")
    public Set<SolicitudApertura> buscarTodos() {
        return (Set<SolicitudApertura>) entityManager()
                .createQuery("from " + SolicitudApertura.class.getName())
                .getResultList();
    }
}
