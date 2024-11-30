package Domain.models.entities.formasDeColaborar;

import Domain.models.entities.MigradorDeDonantes.Fecha;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.personaVulnerable.PersonaVulnerable;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "registro_persona_vulnerable")
public class RegistroPersonaVulnerable extends  FormaDeColaboracion{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn (name = "personaRegsitrada_id", referencedColumnName = "id")
    private PersonaVulnerable personaRegistrada;

    @ManyToOne
    @JoinColumn(name = "colaboradorQueRegistro_id", referencedColumnName = "id")
    private Colaborador colaboradorQueRegistro;

    @Column(name = "fecha", columnDefinition = "DATE")
    private LocalDate fecha;

    @Column(name ="multiplicadorTarjetas")
    private Double multiplicadorTarjetas = 2.0;

    @Column(name ="cantidad")
    private Integer cantidad;


    public RegistroPersonaVulnerable(Colaborador unColaborador, PersonaVulnerable persona){
        personaRegistrada= persona;
        colaboradorQueRegistro = unColaborador;
        fecha = LocalDate.now();
    }

    public RegistroPersonaVulnerable(Integer cantidadEntregada, String fechaDonacion){
        cantidad = cantidadEntregada;
        fecha = LocalDate.now();
    }
    public RegistroPersonaVulnerable() {
    }

    @Override
    public Double calcularPuntos() {
        if (colaboradorQueRegistro == null) {
            throw new IllegalStateException("El colaborador que registró es null");
        }
        return Double.valueOf(this.colaboradorQueRegistro.tarjetasRepartidas() * multiplicadorTarjetas);
    }

    @Override
    public Integer cantidadViandasDonadas(){
        return 0;
    }
}

