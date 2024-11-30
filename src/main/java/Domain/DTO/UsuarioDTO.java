package Domain.DTO;

import Domain.models.entities.Usuario.TipoPermiso;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
    String id;
    String nombreUsuario;
    String habilitado;
    String contrasenia;
    String rol;
    Boolean permisoRegistrarPersonaVulnerable;
    Boolean permisoDistribucionViandas;
    Boolean permisoDonacionViandas;
    Boolean permisoDonacionMonetaria;
    Boolean permisoOfrecerProductos;
    Boolean permisoEncargarseHeladera;
}

