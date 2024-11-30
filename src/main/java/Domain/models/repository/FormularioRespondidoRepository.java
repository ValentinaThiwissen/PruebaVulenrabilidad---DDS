package Domain.models.repository;

import Domain.models.entities.formasDeColaborar.Oferta;
import Domain.models.entities.formulario.FormularioRespondido;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.Optional;
import java.util.Set;

public class FormularioRespondidoRepository implements WithSimplePersistenceUnit {
    public Optional<FormularioRespondido> buscar(Long id) {

        return Optional.ofNullable(entityManager().find(FormularioRespondido.class, id));
    }

    public void guardar(FormularioRespondido formularioRespondido) {
        entityManager().persist(formularioRespondido);
    }
    @SuppressWarnings("unchecked")



    public FormularioRespondido buscarPorID(Long id) {
        return entityManager().find(FormularioRespondido.class, id);
    }

    @SuppressWarnings("unchecked")
    public Set<FormularioRespondido> buscarTodos() {
        return (Set<FormularioRespondido>) entityManager()
                .createQuery("from " + FormularioRespondido.class)
                .getResultList();
    }

    public void actualizar(FormularioRespondido formularioRespondido) {
        withTransaction(() -> {
            entityManager().merge(formularioRespondido);//UPDATE
        });
    }
    
    public void eliminar(FormularioRespondido formularioRespondido) {
        entityManager().remove(formularioRespondido);//DELETE
    }

    public void actualizar2(FormularioRespondido formulario) {
        withTransaction(() -> {
            if (formulario.getId() == null) {
                entityManager().persist(formulario); // INSERT
            } else {
                entityManager().merge(formulario); // UPDATE
            }
        });
    }
}

