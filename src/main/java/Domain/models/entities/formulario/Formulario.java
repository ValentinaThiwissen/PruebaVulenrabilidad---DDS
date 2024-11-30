package Domain.models.entities.formulario;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "formulario")
public class Formulario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombreForm")
    String nombreForm;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn (name= "formulario_id")
    public List <Pregunta> preguntas;

    public  void nuevaPregunta(Pregunta unaPregunta){
        preguntas.add(unaPregunta);
    }

    public Formulario(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }
    public Formulario(){}
}
