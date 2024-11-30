
package Domain.models.entities.personaVulnerable;

import Domain.models.entities.Ubicacion.Direccion;
import Domain.models.entities.colaborador.PersonaARegistrar;
import Domain.models.entities.tarjeta.TarjetaDeHeladeras;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Iterator;
import java.util.Set;
@Setter
@Getter
@Entity
@Table(name ="persona_vulnerable")
public class PersonaVulnerable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;//PK

    @Column(name = "nombrePersona",columnDefinition = "TEXT",nullable = false)
    private String nombrePersona;

    @Column(name = "apellido",columnDefinition = "TEXT")
    private String apellido;

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @Column(name = "nroDocumento")
    private Integer nroDocumento;//tal vez lo deberiamos pasar a un string ya que el pasaporte tiene letras

    @Column(name = "fechaNacimiento", columnDefinition = "DATE",nullable = false)
    private LocalDate fechaDeNacimiento;

    @Column(name = "fechaRegistro", columnDefinition = "DATE",nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "tienePersonasACargo",nullable = false)
    private Boolean tienePersonasACargo;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn (name= "menor_id")
    private Set<PersonaVulnerable> menoresACargo;

    @Column(name= "situacionDeCalle",nullable = false)
    private Boolean situacionDeCalle;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name ="direccion_id")
    private Direccion unDomicilio;

    @OneToOne
    @JoinColumn(name = "tarjetaHeladera_id", referencedColumnName = "id")
    private TarjetaDeHeladeras tarjetaDeHeladera;


    public PersonaVulnerable(PersonaARegistrar persona){

        this.nombrePersona = persona.getNombre();
        this.tipoDocumento = persona.getTipoDocumento();
        this.nroDocumento = persona.getNroDocumento();
        this.fechaDeNacimiento = persona.getFechaNacimiento();
        this.fechaRegistro = LocalDate.now();//hoy
        this.tienePersonasACargo = persona.getTienePersonasAcargo();
        this.menoresACargo = persona.getMenoresACargo();
        this.situacionDeCalle = persona.getSituacionDeCalle();
        this.unDomicilio = persona.getDireccion();
        this.tarjetaDeHeladera = null;
    }
    public  void agregarMenor(PersonaVulnerable personaVulnerable){
        menoresACargo.add(personaVulnerable);
    }

    // SI ES MENOR ENTRA ACA
    public PersonaVulnerable (String nombre, TipoDocumento tipoDocumento, Integer nroDocumento,
                              LocalDate fechaNacimiento, LocalDate fechaRegistro, Boolean situacionDeCalle){

        this.nombrePersona = nombre;
        this.tipoDocumento = tipoDocumento;
        this.nroDocumento = nroDocumento;
        this.fechaDeNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.situacionDeCalle = situacionDeCalle;
    }

    public PersonaVulnerable() {

    }

    /*
    public void pedirTarjeta(Colaborador unColaborador){
         tarjetaDeHeladera = unColaborador.entregarTrajeta(this);
    }
    */

    public Integer cantidadMenoresACargo(){
        //para cada persona vulnerable de la lista se fija si es menor y sino lo saca y despues calcula el tamaño de la lista, ya sin los que no son mas menores
        //menoresACargo.forEach(this::siNoEsMenorSacarlo); de esta forma me tira concurrentModificationExeptrion
        Iterator<PersonaVulnerable> iterator = menoresACargo.iterator();
        while (iterator.hasNext()) {
            PersonaVulnerable persona = iterator.next();
            if (!persona.esMenor()) {
                iterator.remove();
            }
        }
        return this.menoresACargo.size();
    }
    /*public void siNoEsMenorSacarlo(PersonaVulnerable persona){
       if(!persona.esMenor()){
          menoresACargo.remove(persona);
      } // se tendria que convertir en una persona vulnerable en si misma? (tal vez habría que hacer una sola clase persona vulnerable)
   }*/

    // period.between da el tiempo entre dos fechas y despues con get Years saco la edad
    public Integer edadPersona(){
        return Period.between(this.fechaDeNacimiento, LocalDate.now()).getYears();
    }
    public Boolean esMenor(){
        return edadPersona() < 18;
    }




}
