package Domain.controller.HeladeraController;

import Domain.DTO.PuntoGeograficoHeladeraDTO;
import Domain.models.repository.HeladeraRepository;
import Domain.utils.ICrudViewsHandler;
import com.google.gson.Gson;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import io.javalin.http.Context;


public class HeladeraController implements ICrudViewsHandler, WithSimplePersistenceUnit {

    HeladeraRepository heladeraRepository;
    private static final Logger logger = Logger.getLogger(HeladeraController.class.getName());

    public HeladeraController(HeladeraRepository heladeraRepository){

        this.heladeraRepository = heladeraRepository;
    }

    @Override
    public void index(Context context){
        //TODO
    }

    @Override
    public void show(Context context){
        List<PuntoGeograficoHeladeraDTO> heladeras = this.heladeraRepository.buscarTodosPorPuntoGeograficoActiva();

        if (heladeras == null || heladeras.isEmpty()) {
            context.status(404).result("No se encontraron heladeras");
            return;
        }
        context.result(new Gson().toJson(heladeras));

        Map<String, Object> model = new HashMap<>();
        model.put("heladeras", heladeras);
    }

    @Override
    public void update(Context context) {
        //TODO
    }
    @Override
    public void delete(Context context) {
        //TODO
    }
    @Override
    public void edit(Context context) {
        //TODO
    }
    @Override
    public void create(Context context) {
        //TODO
    }

    @Override
    public void save(Context context) {
        //TODO
    }
}
