package Domain.models.repository;

import Domain.controller.ReporteController;
import Domain.models.entities.formasDeColaborar.Canje;
import Domain.models.entities.reporte.reportes.ReporteExportable;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryDelegatingImpl;
import org.hibernate.internal.SessionFactoryImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ReporteRepository implements WithSimplePersistenceUnit {
    ReporteController controller = new ReporteController();

    public Optional<ReporteExportable> buscar(Long id) {

        return Optional.ofNullable(entityManager().find(ReporteExportable.class, id));
    }

    public void guardarCanje(Canje canje) {
        entityManager().persist(canje);
    }
    @SuppressWarnings("unchecked")
    public void guardarReporte(ReporteExportable reporteExportable) {
        beginTransaction();
        entityManager().persist(reporteExportable);
        entityManager().flush();
        commitTransaction();
    }

    public ReporteExportable buscarPorID(Long id) {
        return entityManager().find(ReporteExportable.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<ReporteExportable> buscarTodos() {
        return entityManager()
                .createQuery("from " + ReporteExportable.class.getName())
                .getResultList();
    }

    public void actualizar(ReporteExportable reporte) {
        withTransaction(() -> {
            entityManager().merge(reporte);//UPDATE
        });
    }


    public void eliminar(ReporteExportable reporte) {
        entityManager().remove(reporte);//DELETE
    }
}
