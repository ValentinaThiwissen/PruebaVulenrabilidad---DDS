package Domain.models.repository;

import Domain.models.entities.formasDeColaborar.Canje;
import Domain.models.entities.formulario.Formulario;
import Domain.models.entities.heladera.Heladera;
import com.twilio.twiml.voice.Prompt;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FormularioRepository implements WithSimplePersistenceUnit{
    public Optional<Formulario> buscar(Long id) {

        return Optional.ofNullable(entityManager().find(Formulario.class, id));
    }

    public void guardar(Formulario formulario) {
        entityManager().persist(formulario);
    }
    @SuppressWarnings("unchecked")



    public Formulario buscarPorID(Long id) {
        return entityManager().find(Formulario.class, id);
    }

    @SuppressWarnings("unchecked")

    public List<Formulario> buscarTodos() {
        return (List<Formulario>) entityManager()
                .createQuery("from " + Formulario.class.getName())
                .getResultList();
    }

    public void actualizar(Formulario formulario) {
        withTransaction(() -> {
            entityManager().merge(formulario);//UPDATE
        });
    }

    public void actualizar2(Formulario formulario) {
        withTransaction(() -> {
            if (formulario.getId() == null) {
                entityManager().persist(formulario); // INSERT
            } else {
                entityManager().merge(formulario); // UPDATE
            }
        });
    }


    public void eliminar(Formulario formulario) {
        entityManager().remove(formulario);//DELETE
    }
}

