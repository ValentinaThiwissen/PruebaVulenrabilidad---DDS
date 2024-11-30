package Domain.controller.PersonaVulnerableController;

import Domain.DTO.PersonaVulnerableDTO;
import Domain.Exceptions.AccesDeniedException;
import Domain.models.entities.Ubicacion.Direccion;
import Domain.models.entities.Ubicacion.Localidad;
import Domain.models.entities.Ubicacion.Partido;
import Domain.models.entities.Ubicacion.Provincia;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.personaVulnerable.PersonaVulnerable;
import Domain.models.entities.personaVulnerable.TipoDocumento;
import Domain.models.repository.DireccionRepository;
import Domain.models.repository.PersonaVulnerableRepository;
import Domain.models.repository.RolRepository;
import Domain.models.repository.UsuarioRepository;
import Domain.utils.ICrudViewsHandler;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.time.LocalDate;

public class PersonaVulnerableController  implements ICrudViewsHandler,WithSimplePersistenceUnit {
    PersonaVulnerableRepository personaVulnerableRepository;
    UsuarioRepository usuarioRepository;
    RolRepository rolRepository;
    //  DireccionRepository direccionRepository;
    public  PersonaVulnerableController (PersonaVulnerableRepository personaVulnerableRepository, UsuarioRepository usuarioRepository, RolRepository rolRepository){
        this.personaVulnerableRepository= personaVulnerableRepository;
        this.usuarioRepository =usuarioRepository;
        this.rolRepository = rolRepository;
        //this.direccionRepository = direccionRepository;
    }

    @Override
    public void index(Context context) {

    }

    @Override
    public void show(Context context) {

    }

    @Override
    public void create(Context context) {
    }

    @Override
    public void save(Context context) {

        Usuario usuario = this.usuarioRepository.buscarPorID(context.sessionAttribute("usuario_id"));

        if(usuario == null || !rolRepository.tienePermiso(usuario.getRol().getId(), "registrar-persona-vulnerable")) {
            throw new AccesDeniedException();
        }

        PersonaVulnerable personaVulnerable = new PersonaVulnerable();
        personaVulnerable.setNombrePersona(context.formParam("nombre"));

        String tipoDocumento = context.formParam("tipo_documento");
        if (tipoDocumento.equalsIgnoreCase("DNI")){
            personaVulnerable.setTipoDocumento(TipoDocumento.DNI);}
        else if (tipoDocumento.equalsIgnoreCase("LIBRETAENROLAMIENTO")){
            personaVulnerable.setTipoDocumento(TipoDocumento.LIBRETAENROLAMIENTO);
        }
        else if (tipoDocumento.equalsIgnoreCase("LIBRETACIVICA")){
            personaVulnerable.setTipoDocumento(TipoDocumento.LIBRETACIVICA);
        }

        try {
            int nroDocumento = Integer.parseInt(context.formParam("documento"));
            personaVulnerable.setNroDocumento(Integer.valueOf(nroDocumento));
        } catch (NumberFormatException e) {
            context.status(400).result("Documento inválido");
            return;
        }

        String fechaNacimientoStr = context.formParam("nacimiento");
        if (fechaNacimientoStr != null && !fechaNacimientoStr.isEmpty()) {
            personaVulnerable.setFechaDeNacimiento(LocalDate.parse(fechaNacimientoStr));
        } else {
            System.out.println("El campo 'nacimiento' está vacío o es nulo.");
        }

        String menoresACargoSeleccionado = context.formParam("menores_a_cargo");
        personaVulnerable.setTienePersonasACargo(menoresACargoSeleccionado.equalsIgnoreCase("SI"));

        personaVulnerable.setSituacionDeCalle(false);  // Ajusta según la lógica que necesites

        //esto es cuando dice si, ahi le hace lo de menores acargo
        if (menoresACargoSeleccionado.equalsIgnoreCase("SI")) {
            int cantidadMenores = Integer.parseInt(context.formParam("cantidad_menores"));

        }
        String altura = context.formParam("altura");
        String calle = context.formParam("calle");
        String localidad = context.formParam("localidad");
        String partido_nombre = context.formParam("partido");
        String nombre_provincia = context.formParam("provincia");

        if (altura != null && !altura.isEmpty() && calle != null && !calle.isEmpty()
                && localidad != null && !localidad.isEmpty()
                && partido_nombre != null && !partido_nombre.isEmpty()
                && nombre_provincia != null && !nombre_provincia.isEmpty()) {

            /* Chequear si es necesario ver si ya está creada en la BD**/
            Provincia provincia1= new Provincia();
            provincia1.setNombre(nombre_provincia);

            Partido partido = new Partido();
            partido.setNombre(partido_nombre);
            partido.setProvincia(provincia1);

            Direccion unDomicilio = new Direccion();
            unDomicilio.setCalle(calle);
            unDomicilio.setAltura(Integer.valueOf(altura));



            Localidad localidad1 = new Localidad();
            localidad1.setNombre(localidad);
            unDomicilio.setLocalidad(localidad1);
            personaVulnerable.setUnDomicilio(unDomicilio);

            personaVulnerable.setSituacionDeCalle(false);
        }
        else personaVulnerable.setSituacionDeCalle(true);

        // Guardar la persona vulnerable
        personaVulnerable.setFechaRegistro(LocalDate.now());

        try {
            beginTransaction();
            this.personaVulnerableRepository.guardar(personaVulnerable);
            commitTransaction();

            context.redirect("/success");

        } catch (Exception e) {
            e.printStackTrace();
            context.status(500).result("Ha ocurrido un error inesperado.");
        }
    }

    @Override
    public void edit(Context context) {



    }

    @Override
    public void update(Context context) {

    }

    @Override
    public void delete(Context context) {

    }
}
