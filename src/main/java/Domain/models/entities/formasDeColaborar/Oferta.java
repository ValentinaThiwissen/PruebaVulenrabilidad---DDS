package Domain.models.entities.formasDeColaborar;

import lombok.Getter;
import lombok.Setter;
import retrofit2.http.GET;

import javax.persistence.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name ="Oferta")
public class Oferta {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name ="nombre", columnDefinition = "varchar(255)")
    private String nombre;


    @Column(name ="puntosNecesarios")
    private Integer puntosNecesarios;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name="oferta_id")
    private List<Producto> productos = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name="rubro_id")
    private Rubro rubro;

    @Column(name= "imagen",columnDefinition = "varchar(255)")
    private String imagen; //esto como??

    public Oferta(String nombreOferta, Rubro rubro){
        this.nombre = nombreOferta;
        this.puntosNecesarios = 5;//le pise no
        this.rubro = rubro;
    }
    public Oferta(String nombreOferta){
        this.nombre = nombreOferta;
        this.puntosNecesarios = 5;//le pise n
    }

    public Oferta() {

    }
}
