package Domain.models.repository;

import Domain.models.entities.personaVulnerable.PersonaVulnerable;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PersonaVulnerableRepository  implements WithSimplePersistenceUnit {

    public Optional<PersonaVulnerable> buscar(Long id) {

        return Optional.ofNullable(entityManager().find(PersonaVulnerable.class, id));


    }
    @SuppressWarnings("unchecked")
    public List<PersonaVulnerable> buscarPorNombre(String nombre) {
        return entityManager()
                .createQuery("from " + PersonaVulnerable.class.getName() + " where nombre =:name")
                .setParameter("name", nombre)
                .getResultList();
    }
    public void guardar(PersonaVulnerable persona) {
        entityManager().persist(persona);
    }


    public void actualizar(PersonaVulnerable persona) {
        withTransaction(() -> {
            entityManager().merge(persona);//UPDATE
        });
    }public void eliminar(PersonaVulnerable persona) {
        entityManager().remove(persona);//DELETE
    }
    @SuppressWarnings("unchecked")
    public Set<PersonaVulnerable> buscarTodos() {
        return (Set<PersonaVulnerable>) entityManager()
                .createQuery("from " + PersonaVulnerable.class.getName())
                .getResultList();
    }

}
