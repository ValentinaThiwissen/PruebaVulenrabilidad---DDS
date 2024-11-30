package Domain.models.entities.Usuario;

import Domain.models.entities.formasDeColaborar.FormaDeColaboracion;
import Domain.models.entities.formasDeColaborar.TiposDeColaboracion;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Data
@Entity
@Table(name ="rol")
public class Rol {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoRol tipo;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rol_tipoPermiso", joinColumns = @JoinColumn(name = "rol_id",referencedColumnName="id"))
    @Column(name = "permisos")
    private List<TipoPermiso> permisos;

    public Rol() {
        this.permisos = new ArrayList<>();
    }

    public boolean tenesPermiso(Optional<FormaDeColaboracion> permiso) {
        return this.permisos.contains(permiso);
    }

    public Boolean tienePermiso(TipoPermiso permiso){
        return this.permisos.contains(permiso);
    }
}
