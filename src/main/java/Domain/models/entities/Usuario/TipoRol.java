package Domain.models.entities.Usuario;


import io.javalin.security.RouteRole;

public enum TipoRol implements RouteRole {
    PERSONA_HUMANA,
    PERSONA_JURIDICA,
    ADMIN,
    TECNICO
}
