package Domain.models.entities.Ubicacion;

import lombok.Data;

import javax.persistence.*;

@Data
//@Table(name = "punto_geografico")
//@Entity
@Embeddable
public class PuntoGeografico {
     @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
     @JoinColumn(name ="direccion_id", referencedColumnName = "id")
     private Direccion direccion;

     @Column
     private Double latitud;

     @Column
     private Double longitud;

     public PuntoGeografico(double latitud, double longitud){
          this.latitud = latitud;
          this.longitud = longitud;
     }

     public PuntoGeografico() {
     }
}
