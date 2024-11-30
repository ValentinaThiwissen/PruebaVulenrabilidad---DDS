package Domain.models.entities.formasDeColaborar;

import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.tarjeta.TarjetaDistribucionViandas;
import Domain.models.entities.vianda.Vianda;
import Domain.models.suscripciones.Suscripcion;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "distribucion_viandas")
public class DistribucionViandas extends FormaDeColaboracion{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @ManyToOne
   @JoinColumn(name = "heladeraDistribucionViandas_id", referencedColumnName = "id")
   private Heladera heladeraOrigen;

   @OneToMany
   @JoinColumn(name= "distribucionViandaDestino_id")
   private Set<Heladera> heladerasDestino = new HashSet<>();

   @Enumerated(EnumType.STRING)
   private MotivoDistribucion motivoDistribucion;

   @Column(name = "multiplicadorDistribucion")
   private Double multiplicadorDistribucion = 1.0;

   @OneToMany
   @JoinColumn(name= "viandasAMover_id")
   private Set<Vianda> viandasAMover;

   @Column(columnDefinition = "DATE")
   private LocalDate fecha;

   @Column(name = "viandasMovidas")
   private Integer viandasMovidas = 0;

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
   @JoinColumn(name = "tarjetaDistribucion_id", referencedColumnName = "id")
   private TarjetaDistribucionViandas tarjeta;

   public DistribucionViandas(Integer cantidadViandas, String fechaDonacion){
       viandasMovidas = cantidadViandas;
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
       fecha = LocalDate.parse(fechaDonacion, formatter);
   }

    public DistribucionViandas(Suscripcion suscripcion, TarjetaDistribucionViandas unaTarjeta, Integer num){
        fecha = LocalDate.now();
        tarjeta = unaTarjeta;
    }

   public DistribucionViandas(Suscripcion suscripcion, TarjetaDistribucionViandas unaTarjeta){
       fecha = LocalDate.now();
       tarjeta = unaTarjeta;
       heladeraOrigen = suscripcion.getHeladeraOrigen();
       heladerasDestino = suscripcion.getHeladerasDestino();
       motivoDistribucion = suscripcion.getMotivo();//VER LO DE LA SOLCIITUDES DE APERTURA
       viandasMovidas = suscripcion.getViandasADistribuir();
       viandasAMover = heladeraOrigen.abrirHeladeraYSacarViandas(viandasMovidas, tarjeta);
       heladerasDestino.forEach(heladera -> heladera.abrirHeladeraYAgregarViandas(viandasAMover, tarjeta));
   }

    public DistribucionViandas() {

    }

    public void sumarViandas(Integer cantidad){
        this.viandasMovidas += cantidad;
    }

    @Override
    public Integer cantidadViandasDonadas(){
       return 0;
   }
    public int cantidadAMover(){
       return viandasMovidas;
   }
    @Override
    public Double calcularPuntos() {
       return Double.valueOf(this.cantidadAMover() * multiplicadorDistribucion);
    }

    public void agregarHeladeraDestino(Heladera heladera){
       this.heladerasDestino.add(heladera);
    }

}
