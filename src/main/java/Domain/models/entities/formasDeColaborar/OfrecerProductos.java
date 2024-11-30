package Domain.models.entities.formasDeColaborar;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
@Getter
@Setter
@Entity
@Table(name = "ofrecer_productos")
public class OfrecerProductos extends FormaDeColaboracion{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name= "productosOfrecidos_id")
    private Set<Oferta> ofertasDisponibles= new HashSet<>();**/

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name= "productosOfrecidos_id")
    private Oferta ofertasDisponibles;

    @Column(columnDefinition = "DATE")
    private LocalDate fechaOfertas;

    public OfrecerProductos(Oferta ofertasDisponibles, LocalDate fechaOfertas) {
        this.ofertasDisponibles = ofertasDisponibles;
        this.fechaOfertas = fechaOfertas;
    }

    public OfrecerProductos() {

    }

    @Override
    public Double calcularPuntos() {
        return 0.0;
    }

    @Override
    public Integer cantidadViandasDonadas(){
        return 0;
    }

}
