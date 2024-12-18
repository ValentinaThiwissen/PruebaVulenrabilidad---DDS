package Domain.models.repository;

import Domain.models.entities.Usuario.Rol;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import Domain.models.entities.Usuario.TipoRol;
import javax.persistence.EntityTransaction;
public class RolRepository implements WithSimplePersistenceUnit {
    public void registrar(Rol rol)
    {
        entityManager().persist(rol);
    }

    public List<Rol> todos() {
        return entityManager()
                .createQuery("from Rol")
                .getResultList();
    }

    public Rol buscarPorID(Long id) {
        return entityManager().find(Rol.class, id);
    }
    public Rol buscarPorTipoRol(TipoRol tipoRol) {
        Rol rol = entityManager().createQuery(
                        "SELECT r FROM Rol r WHERE r.tipo = :tiporol",Rol.class)
                .setParameter("tiporol", tipoRol)
                .getSingleResult();
        return rol;
    }

    public TipoRol buscarTipoRol(Long idRol ) {
        Rol rol = buscarPorID(idRol);
        TipoRol trol = rol.getTipo();
        return trol;
    }

    public boolean tienePermiso(Long idRol, String nombreInterno) {

        Long count = (Long) entityManager().createQuery(
                        "SELECT COUNT(p) FROM Rol r JOIN r.permisos p WHERE r.id = :idRol AND p.nombreInterno = :nombreInterno", Long.class)
                .setParameter("idRol", idRol)
                .setParameter("nombreInterno", nombreInterno)
                .getSingleResult();
        return count > 0;
    }


    public List<Rol> all() {
        return entityManager().createQuery("from Rol", Rol.class).getResultList();
    }

    public void persistir(List<Rol> roles) {
        EntityTransaction tx = entityManager().getTransaction();
        tx.begin();
        for (Rol r : roles) {
            entityManager().merge(r);
        }
        tx.commit();
    }


}
