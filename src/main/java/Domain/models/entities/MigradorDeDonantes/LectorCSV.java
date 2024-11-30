package Domain.models.entities.MigradorDeDonantes;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.colaborador.TipoColaborador;
import Domain.models.entities.formasDeColaborar.*;
import Domain.models.entities.notificacion.NotificarEmail;
import Domain.models.repository.ColaboradorRepository;
import Domain.models.repository.UsuarioRepository;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.*;

@Getter
@Setter
public class LectorCSV {
    private static String filePath;
    private NotificarEmail enviadorMail = new NotificarEmail();
    private Set<Colaborador> usuariosNuevos;
    public LectorCSV(String filePath) {
        LectorCSV.filePath = filePath;
    }
    ColaboradorRepository repositorio  = new ColaboradorRepository();

    private static List<String[]> obtenerFilas() {
        List<String[]> filas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                filas.add(datos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filas;
    }
    public List<Colaborador>  leerUsuarios() {
        List<String[]> filas = obtenerFilas();
        Map<Integer, Colaborador> colaboradoresMap = new LinkedHashMap<>();
        for(String[] fila : filas){
            Integer nroDni = Integer.parseInt(fila[1]);
            if(!colaboradoresMap.containsKey(nroDni)){
                FormaDeColaboracion forma = identificarFormaDeColaborar(fila);
                Colaborador colaborador = getColaborador(fila, forma);
                colaborador.setTipoColaborador(TipoColaborador.PERSONAHUMANA);
                colaboradoresMap.put(nroDni,colaborador);
            }else{
                Colaborador colaboradorExistente = colaboradoresMap.get(nroDni);
                actualizarFormaDeColaboracion(colaboradorExistente, fila);
            }

        }

        return new ArrayList<>(colaboradoresMap.values());
    }

    private void actualizarFormaDeColaboracion(Colaborador colaboradorExistente, String[] fila) {
        FormaDeColaboracion formaAAgregar = identificarFormaDeColaborar(fila);
        colaboradorExistente.agregarColaboracion(formaAAgregar);
    }
    private Colaborador getColaborador(String[] datos, FormaDeColaboracion forma){
        return new Colaborador(datos, forma);
    }

    public FormaDeColaboracion identificarFormaDeColaborar(String[] datos){
        Integer cantidad = Integer.parseInt(datos[7]);
        return switch (datos[6]) {
            case "DINERO" -> new DonacionMonetaria(cantidad, datos[5]);
            case "DONACION_VIANDAS" -> new DonacionViandas(cantidad, datos[5]);
            case "REDISTRIBUCION_VIANDAS" -> new DistribucionViandas(cantidad   , datos[5]);
            case "ENTREGA_TARJETAS"-> new RegistroPersonaVulnerable(cantidad, datos[5]);

            default -> null;
        };
    }
    public List<Colaborador> obtenerUsuariosNuevos(List<Colaborador> colaboradores){
        /*Set<String> dnisExistentes = repositorio.obtenerTodosLosDNIs();
        return colaboradores.stream()
                .filter(colaborador -> !dnisExistentes.contains(colaborador.getNroDni()))
                .toList();*/
        return colaboradores;
    }

}
