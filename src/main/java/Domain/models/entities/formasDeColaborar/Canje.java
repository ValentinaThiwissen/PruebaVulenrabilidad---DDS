package Domain.models.entities.formasDeColaborar;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
@Data
@Setter
@Getter
@Entity
@Table(name = "canje")
public class Canje {
    //esto hya que persistir
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fechaCanje", columnDefinition = "DATE")
    private LocalDate fechaCanje;

    @Column
    private Double precioCanje;

    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "oferta_id")
    private  Oferta oferta;


    public Integer cantidadViandasDonadas(){
        return 0;
    }

    public Integer puntosNecesarios(){
        return oferta.getPuntosNecesarios();
    }


}
