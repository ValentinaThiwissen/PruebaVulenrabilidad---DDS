package Domain.models.entities.formasDeColaborar;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@MappedSuperclass

/**
@Entity
@Inheritance(strategy = InheritanceType.JOINED)  // O SINGLE_TABLE, dependiendo de tus necesidades
public abstract class FormaDeColaboracion {
    // ...
}*/

//@Table(name= "formadecolaboracion")
@Entity @Table(name="forma_de_colaboracion")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class  FormaDeColaboracion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    //cada uno va a tener sus propios generated id
    private Long id;


    public abstract Double calcularPuntos();

    public abstract  Integer cantidadViandasDonadas();

    public Long getId(){
        return id;
    }
}
