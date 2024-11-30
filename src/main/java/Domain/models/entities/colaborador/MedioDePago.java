package Domain.models.entities.colaborador;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "MedioDePago")

public class MedioDePago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    TipoMedioPago tipoMedioPago;

    @Column(name="numeroTarjeta")
    Integer numeroTarjeta;
}