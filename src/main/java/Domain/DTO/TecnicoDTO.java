package Domain.DTO;

import lombok.Data;

@Data
public class TecnicoDTO {
        private String nombre;
        private double latitud;
        private double longitud;
        private double radioDeCobertura;

        public TecnicoDTO(String nombre, double latitud, double longitud, double radioDeCobertura) {
            this.nombre = nombre;
            this.latitud = latitud;
            this.longitud = longitud;
            this.radioDeCobertura = radioDeCobertura;
        }
}
