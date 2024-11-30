package Domain.models.entities.calculadoras;

import Domain.models.entities.Ubicacion.Direccion;
import Domain.models.entities.Ubicacion.PuntoGeografico;
import Domain.models.entities.tecnico.Tecnico;
import Domain.services.recomendacionPuntosApi.entities.DatosGeograficos;

import java.util.Set;

public class BuscadorTecnicos {
    private CalculadoraDistancia calculador;
/*
    public Tecnico buscarTecnicoMasCercano(Set<Tecnico> tecnicos, PuntoGeografico puntoGeografico) {
        Tecnico tecnicoMasCercano = null;
        double distanciaMinima = Double.MAX_VALUE;
        double distancia;

        for (Tecnico tecnico : tecnicos) {
            distancia = CalculadoraDistancia.calcularDistancia(tecnico.getAreaDeCobertura(), puntoGeografico);
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                tecnicoMasCercano = tecnico;
            }
        }

        return tecnicoMasCercano;
    }*/
}
