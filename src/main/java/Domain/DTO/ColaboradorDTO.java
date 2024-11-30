package Domain.DTO;

import Domain.models.entities.Ubicacion.Direccion;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.MedioDeContacto;
import Domain.models.entities.colaborador.TipoColaborador;
import Domain.models.entities.colaborador.TipoJuridico;
import Domain.models.entities.formasDeColaborar.FormaDeColaboracion;
import Domain.models.entities.formasDeColaborar.Oferta;
import Domain.models.entities.formasDeColaborar.TiposDeColaboracion;
import Domain.models.entities.formulario.FormularioRespondido;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.personaVulnerable.TipoDocumento;
import Domain.models.entities.tarjeta.TarjetaDeHeladeras;
import Domain.models.entities.tarjeta.TarjetaDistribucionViandas;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ColaboradorDTO {
    private String nombreUsuario;
    private String tipoColaborador;
    private String tipoJuridico;
    private String razonSocial;
    private String nombre;
    private String apellido;
    private String unaDireccion;
    private String tipoDni;
    private String nroDni;
    private String nacimiento;
    private String mail;


    public ColaboradorDTO(String tipoColaborador, String nombre, String apellido, String unaDireccion, String tipoDni, String nroDni, String nacimiento, String mail, String tipoJuridico) {
        this.tipoColaborador = tipoColaborador;
        this.nombre = nombre;
        this.apellido = apellido;
        this.unaDireccion = unaDireccion;
        this.tipoDni = tipoDni;
        this.nroDni = nroDni;
        this.nacimiento = nacimiento;
        this.mail = mail;
        this.tipoJuridico = tipoJuridico;
    }

    public ColaboradorDTO() {

    }
}
