package Domain.DTO;

import lombok.Data;

import java.time.LocalDate;
@Data
public class PersonaVulnerableDTO {
    //Pongo la info que iria en el formulairo pero en formato string
    private String nombrePersona;
    private String tipoDocumento;
    private Integer nroDocumento;
    private String fechaDeNacimiento;
    private LocalDate fechaRegistro;
    private String  tienePersonasACargo;
    private Integer menoresACargo;
    private String situacionDeCalle;
    private String unDomicilio;


}
