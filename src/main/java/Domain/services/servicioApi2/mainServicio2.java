package Domain.services.servicioApi2;

import Domain.DTO.ColaboradorSolicitadoDTO;
//import Domain.main.MainExample;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class mainServicio2 implements WithSimplePersistenceUnit {

    @SneakyThrows
    public static  void main(String[] args){
       // MainExample instance = new MainExample();
        buscadorDeColaboradores instante = buscadorDeColaboradores.obtenerInstancia();
        //TODO esta hardckodeado tiene q agarrarlo de la pagina
        int minPuntos = 1;
        int minDonacionUltMes = 10;
        int cantAMostrar = 5;

        List<ColaboradorSolicitadoDTO> colaboradores = instante.buscarColaboradores(minPuntos, minDonacionUltMes, cantAMostrar);

        if (colaboradores != null){
            System.out.println("Se encontraron los siguientes colaboradores /n");
            for (ColaboradorSolicitadoDTO colaborador : colaboradores) {
                System.out.println("Nombre: " + colaborador.getNombre());
                System.out.println("Apellido: " + colaborador.getApellido());
                System.out.println("Email: " + colaborador.getEmail());
                System.out.println("Puntaje: " + colaborador.getPuntaje());
                System.out.println("Cantidad Donaciones: " + colaborador.getCantidadDonaciones());
                System.out.println("-------------");
            }
        }
    }
}