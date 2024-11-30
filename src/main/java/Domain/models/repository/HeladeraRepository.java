package Domain.models.repository;

import Domain.DTO.PuntoGeograficoHeladeraDTO;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.formulario.Formulario;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.reporte.reportes.ReporteExportable;
import Domain.models.entities.tecnico.Tecnico;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class HeladeraRepository  implements WithSimplePersistenceUnit {
    public Heladera buscar(Long id) {

        return entityManager().find(Heladera.class, id);
    }
    private static HeladeraRepository instancia;





    public static HeladeraRepository getInstancia() {
        if (instancia == null) {
            instancia = new HeladeraRepository();
        }
        return instancia;
    }

    public void guardar(Heladera heladera) {
        entityManager().persist(heladera);
    }
    @SuppressWarnings("unchecked")
    /*public Heladera buscarPorNombre(String nombre) {
        return (Heladera) entityManager()
                .createQuery("from " + Heladera.class.getName() + " where nombreHeladera =:name")
                .setParameter("name", nombre)
                .getResultList();
    }*/
    public Heladera buscarPorNombre(String nombre) {
        List<Heladera> resultados = entityManager()
                .createQuery("from " + Heladera.class.getName() + " where LOWER(nombreHeladera) = LOWER(:name)", Heladera.class)
                .setParameter("name", nombre.trim())
                .getResultList();

        // Verificamos si la lista está vacía y retornamos null en ese caso
        if (resultados.isEmpty()) {
            return null;
        } else {
            // Retornamos el primer resultado de la lista (se asume que los nombres de heladeras son únicos)
            return resultados.get(0);
        }
    }

    public Heladera buscarPorID(Long id) {
        return entityManager().find(Heladera.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Heladera> buscarTodos() {
        return entityManager()
                .createQuery("from "+ Heladera.class.getName()) // Nombre de la entidad esperado
                .getResultList();
    }


    public List<Heladera> buscarPorEstado(boolean estadoActivo) {
        String jpql = "SELECT h FROM Heladera h WHERE h.estaActiva = :estadoActivo";
        return entityManager().createQuery(jpql, Heladera.class)
                .setParameter("estadoActivo", estadoActivo)
                .getResultList();
    }

    /*public List<PuntoGeograficoHeladeraDTO> buscarTodosPorPuntoGeograficoActiva() {
        String jpql = new StringBuilder()
                .append("SELECT new Domain.DTO.PuntoGeograficoHeladeraDTO(h.nombreHeladera, h.latitud, h.longitud) ")
                .append("FROM Domain.models.entities.heladera.Heladera h")
                .append("where h.activa = true")
                .toString();

        return entityManager().createQuery(jpql, PuntoGeograficoHeladeraDTO.class).getResultList();
    }*/
    public List<PuntoGeograficoHeladeraDTO> buscarTodosPorPuntoGeograficoActiva() {
        /*String jpql = new StringBuilder()
                .append("SELECT new Domain.DTO.PuntoGeograficoHeladeraDTO(h.nombreHeladera, h.latitud, h.longitud) ")
                .append("FROM Domain.models.entities.heladera.Heladera h")
                .append("where h.activa = true")
                .toString();*/
        String jpql = "SELECT new Domain.DTO.PuntoGeograficoHeladeraDTO(h.nombreHeladera, h.unPuntoGeografico.latitud, h.unPuntoGeografico.longitud) " +
                "FROM Heladera h WHERE activa = true";

        return entityManager().createQuery(jpql, PuntoGeograficoHeladeraDTO.class).getResultList();
    }

    public void actualizar(Heladera heladera) {
        withTransaction(() -> {
            entityManager().merge(heladera);//UPDATE
        });
    }

    public void actualizar2(Heladera heladera) {
        withTransaction(() -> {
            if (heladera.getId() == null) {
                entityManager().persist(heladera); // INSERT
            } else {
                entityManager().merge(heladera); // UPDATE
            }
        });
    }

    public void eliminar(Tecnico tecnico) {
        EntityManager em;
        em = entityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin(); // Iniciar transacción

            if (!em.contains(tecnico)) {
                tecnico = em.merge(tecnico); // Asociar al contexto si está "detached"
            }

            em.remove(tecnico); // Eliminar la entidad

            transaction.commit(); // Confirmar la transacción
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // Revertir la transacción en caso de error
            }
            e.printStackTrace();
        }
    }

}


