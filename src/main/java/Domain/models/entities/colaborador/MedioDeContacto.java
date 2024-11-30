package Domain.models.entities.colaborador;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity
@Table(name= "MedioDeContacto")
@Getter
@Setter
public class MedioDeContacto {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;//PK

    @Column(name = "contacto", columnDefinition ="TEXT")
    private String contacto;

    @Enumerated(EnumType.STRING)
    private TipoDeContacto tipo;

    public MedioDeContacto(String contacto, TipoDeContacto tipo) {
        this.contacto = contacto;
        this.tipo = tipo;
    }

    public MedioDeContacto() {
    }
}