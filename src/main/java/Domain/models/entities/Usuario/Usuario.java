package Domain.models.entities.Usuario;

import Domain.models.repository.UsuarioRepository;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name ="usuario")
@Getter
@Setter
public class Usuario {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "nombreDeUsuario", columnDefinition =  "varchar(255)")
    private String nombreDeUsuario;

    @Column(name ="contraseña", columnDefinition =  "varchar(255)")
    private String contrasenia;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name ="rol_id")
    private Rol rol;

    @Column
    private Boolean habilitado =true;

    public Boolean esMiContrasena(String contrasenaIngresada){
        return contrasenia.equals(contrasenaIngresada);
    }

    public Usuario(String nombre, String contraseniaEncriptada, Rol rol) {
        this.nombreDeUsuario = nombre;
        this.contrasenia = contraseniaEncriptada;
        this.rol = rol;
    }
    public Usuario() {

    }

    public Long getId() {
        return  id;
    }

    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }
}
