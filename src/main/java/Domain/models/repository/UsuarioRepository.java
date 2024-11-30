package Domain.models.repository;

import Domain.models.entities.Usuario.Rol;
import Domain.models.entities.Usuario.TipoPermiso;
import Domain.models.entities.Usuario.TipoRol;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.Modelo;
import Domain.models.entities.tecnico.Tecnico;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import javax.persistence.Query;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsuarioRepository  implements WithSimplePersistenceUnit {
    public void registrar(Usuario usuario) {
        EntityManager em = entityManager();
        em.getTransaction().begin();
        em.persist(usuario);
        em.getTransaction().commit();
        em.close();
    }

    public List<Usuario> todos() {
        EntityManager em = entityManager();
        List<Usuario> usuarios = em.createQuery("from Usuario").getResultList();
        em.close();
        return usuarios;
    }

    public Usuario buscarPorID(Long id) {
        EntityManager em = entityManager();
        Usuario usuario = em.find(Usuario.class, id);
        em.close();
        return usuario;
    }

    public Usuario buscarPorNombreUsuario(String usuario) {
        List<Usuario> resultados = entityManager()
                .createQuery("FROM " + Usuario.class.getName() + " WHERE nombreDeUsuario = :usuario", Usuario.class)
                .setParameter("usuario", usuario.trim())
                .getResultList();
        if (resultados.isEmpty()) {
            return null;
        } else {
            return resultados.get(0);
        }
    }

    public boolean existeUsuarioConNombre(String nombre) {
        String jpql = "SELECT count(u) FROM Usuario u WHERE u.nombre = :nombre";
        TypedQuery<Long> query = entityManager().createQuery(jpql, Long.class);
        query.setParameter("nombre", nombre);
        return query.getSingleResult() > 0;
    }

    public Usuario autenticar(String username, String password) {
        Usuario usuario = buscarPorNombreUsuario(username);
        if (usuario != null && usuario.getContrasenia().equals(password)) {
            return usuario;
        }
        return null;
    }

    public void eliminar(Usuario usuario) {
        EntityManager em = entityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            Query query = em.createQuery("DELETE FROM Colaborador c WHERE c.usuario.id = :usuarioId");
            query.setParameter("usuarioId", usuario.getId());
            query.executeUpdate();

            // Ahora eliminar el usuario
            em.remove(em.contains(usuario) ? usuario : em.merge(usuario));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }
    public void updateUsuario(Usuario usuario) {
        EntityManager em = entityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.merge(usuario); // Actualiza el usuario en la base de datos
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    public Usuario generarUsuarioPara(Colaborador colaborador) {
        Usuario usuario = new Usuario();
        usuario.setNombreDeUsuario(colaborador.getNombre() + colaborador.getApellido());
        usuario.setContrasenia("Seguridad.2024");
        List<TipoPermiso> permisos = new ArrayList<>(Arrays.asList(
                TipoPermiso.DISTRIBUCIONVIANDAS,
                TipoPermiso.DONACIONMONETARIA,
                TipoPermiso.REGISTROPERSONAVULNERABLE,
                TipoPermiso.DONACIONVIANDAS
        ));
        Rol rol = new Rol();
        rol.setTipo(TipoRol.PERSONA_HUMANA);
        rol.setPermisos(permisos);
        usuario.setRol(rol);
        return usuario;
    }

    public void guardar(Usuario usuario) {
        EntityManager em = entityManager();
        beginTransaction();
        em.persist(usuario);
        commitTransaction();
    }

    public void actualizar2(Usuario usuario) {
        withTransaction(() -> {
            if (usuario.getId() == null) {
                entityManager().persist(usuario); // INSERT
            } else {
                entityManager().merge(usuario); // UPDATE
            }
        });
    }
}
