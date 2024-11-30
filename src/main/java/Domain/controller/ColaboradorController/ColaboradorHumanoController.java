package Domain.controller.ColaboradorController;

import Domain.models.entities.Ubicacion.Direccion;
import Domain.models.entities.Ubicacion.Localidad;
import Domain.models.entities.Ubicacion.Partido;
import Domain.models.entities.Ubicacion.Provincia;
import Domain.models.entities.Usuario.*;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.colaborador.MedioDeContacto;
import Domain.models.entities.colaborador.TipoColaborador;
import Domain.models.entities.colaborador.TipoDeContacto;
import Domain.models.entities.formasDeColaborar.FormaDeColaboracion;
import Domain.models.entities.formasDeColaborar.TiposDeColaboracion;
import Domain.models.entities.personaVulnerable.TipoDocumento;
import Domain.models.entities.tarjeta.TarjetaDeHeladeras;
import Domain.models.entities.tarjeta.TarjetaDistribucionViandas;
import Domain.models.repository.*;
import Domain.utils.ICrudViewsHandler;
import Domain.server.Handlers.AccessDeniedHandler;


import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import org.springframework.cglib.core.Local;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

public class ColaboradorHumanoController implements ICrudViewsHandler, WithSimplePersistenceUnit {
    ColaboradorRepository colaboradorRepository;
    UsuarioRepository usuarioRepository;
    RolRepository rolRepository;
    TarjetaHeladerasRepository tarjetaHeladerasRepository;

    TarjetaDistribucionViandaRepository tarjetaDistribucionViandaRepository;

    public ColaboradorHumanoController(ColaboradorRepository colaboradorRepository, UsuarioRepository usuarioRepository, RolRepository rolRepository, TarjetaHeladerasRepository tarjetaHeladerasRepository, TarjetaDistribucionViandaRepository tarjetaDistribucionViandaRepository) {
        this.colaboradorRepository = colaboradorRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.tarjetaHeladerasRepository = tarjetaHeladerasRepository;
        this.tarjetaDistribucionViandaRepository = tarjetaDistribucionViandaRepository;
    }
    private static final Logger logger = Logger.getLogger(ColaboradorHumanoController.class.getName());


    @Override
    public void index(Context context) {

    }


    @Override
    public void show(Context context) {

    }
    public void edit (Context context){
//TODO
    }

    @Override
    public void update (Context context){
//TODO
    }

    @Override
    public void delete (Context context){
//TODO
    }
    @Override
    public void create(Context context) {
        context.render("registrarsePersonaHumana.hbs");
    }


    public void createHumana(Context context) {
        Long id = context.sessionAttribute("usuario_id");
        if (id == null) {
            context.redirect("/inicioSesion");
            return;
        }
        Usuario usuario = usuarioRepository.buscarPorID(id);
        if (usuario == null) {
            context.status(404).result("Usuario no encontrado");
            return;
        }
        String nombreUsuario = usuario.getNombreDeUsuario();

        Map<String, Object> model = new HashMap<>();
        model.put("nombreUsuario", nombreUsuario);

        context.render("personaHumana.hbs", model);
    }

    @Override
    public void save(Context context) {

        /* Usuario usuario = this.usuarioRepository.buscarPorID(context.sessionAttribute("usuario_id"));

        if(usuario == null || !rolRepository.tienePermiso(usuario.getRol().getId(), "re")) {
            throw new AccesDeniedException();
        }*/
        Colaborador colaborador = new Colaborador();
        colaborador.setTipoColaborador(TipoColaborador.PERSONAHUMANA);

        colaborador.setNombre(context.formParam("nombre"));
        colaborador.setApellido(context.formParam("apellido"));

        String tipoDocumento = context.formParam("tipo_documento");
        if (tipoDocumento.equalsIgnoreCase("DNI")) {
            colaborador.setTipoDni(TipoDocumento.DNI);
        } else if (tipoDocumento.equalsIgnoreCase("LIBRETAENROLAMIENTO")) {
            colaborador.setTipoDni(TipoDocumento.LIBRETAENROLAMIENTO);
        } else if (tipoDocumento.equalsIgnoreCase("LIBRETACIVICA")) {
            colaborador.setTipoDni(TipoDocumento.LIBRETACIVICA);
        }
        try {
            int nroDocumento = Integer.parseInt(context.formParam("documento"));
            colaborador.setNroDni(nroDocumento);
        } catch (NumberFormatException e) {
            context.status(400).result("Documento inválido");
            return;
        }

        colaborador.setMail(context.formParam("mail"));

        String fechaNacimientoStr = context.formParam("nacimiento");
        if (fechaNacimientoStr != null && !fechaNacimientoStr.isEmpty()) {
            colaborador.setNacimiento(LocalDate.parse(fechaNacimientoStr));
        } else {
            System.out.println("El campo 'nacimiento' está vacío o es nulo.");
        }

        Set<String> formasDeColaborarSeleccionadas = new HashSet<>(context.formParams("formaColaborar[]"));
        Set<TiposDeColaboracion> formasDeColaborar = new HashSet<>();
        List<TipoPermiso> tipoPermisos = new ArrayList<>();

        Set<TarjetaDeHeladeras> tarjetas = new HashSet<>();
        colaborador.setTarjetas(tarjetas);

        colaborador.setPuntos(0.0);

        for (String forma : formasDeColaborarSeleccionadas) {
            try {
                TiposDeColaboracion formaEnum = TiposDeColaboracion.valueOf(forma);
                formasDeColaborar.add(formaEnum);

                TipoPermiso tipoPermiso = TipoPermiso.valueOf(forma);
                tipoPermisos.add(tipoPermiso);

                if(formaEnum.equals(TiposDeColaboracion.REGISTROPERSONAVULNERABLE)){
                    for(int i = 0; i < 5; i++){
                        TarjetaDeHeladeras tarjeta = new TarjetaDeHeladeras();
                        colaborador.getTarjetas().add(tarjeta);

                        beginTransaction();
                        this.tarjetaHeladerasRepository.guardar(tarjeta);
                        commitTransaction();
                    }
                }

                if(formaEnum.equals(TiposDeColaboracion.DISTRIBUCIONVIANDAS)){
                    TarjetaDistribucionViandas tarjetaDistribucionViandas = new TarjetaDistribucionViandas();
                    String codigo = tarjetaDistribucionViandas.obtenerCodigo(colaborador);
                    tarjetaDistribucionViandas.setCodigoId(codigo);

                    beginTransaction();
                    this.tarjetaDistribucionViandaRepository.guardar(tarjetaDistribucionViandas);
                    commitTransaction();

                    colaborador.setTarjetaDistribucionViandas(tarjetaDistribucionViandas);
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Forma de colaborar inválida: " + forma);
            }
        }
        colaborador.setFormasDisponiblesDeColaborar(formasDeColaborar);
        colaborador.setCanjesRealizados(new HashSet<>());

        List<String> contactos = context.formParams("contacto[]");
        List<String> tiposContacto = context.formParams("tipo_contacto[]");

        if (contactos != null && tiposContacto != null && contactos.size() == tiposContacto.size()) {
            if (colaborador.getMedios() == null) {
                colaborador.setMedios(new HashSet<>());
            }

            int maxContactos = Math.min(contactos.size(), 5);
            for (int i = 0; i < maxContactos; i++) {
                String contacto = contactos.get(i);
                String tipoContacto = tiposContacto.get(i);

                if (contacto != null && !contacto.isEmpty() && tipoContacto != null) {
                    try {
                        // Convertir el tipo de contacto a enum
                        TipoDeContacto tipoDeContacto = TipoDeContacto.valueOf(tipoContacto.toUpperCase());
                        MedioDeContacto medioDeContacto = new MedioDeContacto(contacto, tipoDeContacto);
                        colaborador.getMedios().add(medioDeContacto);

                    } catch (IllegalArgumentException e) {
                        System.out.println("Tipo de contacto no válido: " + tipoContacto);
                    }
                }
            }
            String altura = context.formParam("altura");
            String calle = context.formParam("calle");
            String localidad = context.formParam("localidad");
            String partido_nombre = context.formParam("partido");
            String nombre_provincia = context.formParam("provincia");

            if ((altura != null && !altura.isEmpty() && calle != null && !calle.isEmpty()
                    && localidad != null && !localidad.isEmpty()
                    && partido_nombre != null && !partido_nombre.isEmpty()
                    && nombre_provincia != null && !nombre_provincia.isEmpty())) {// tiene que cumplir las dos
                Provincia provincia1 = new Provincia();
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
                colaborador.setUnaDireccion(unDomicilio);
            }
            //colaborador.setColaboraciones(colaboraciones);

            String nombreUsuario = context.formParam("usuario");
            String contraseniaUsuario = context.formParam("contrasenia");

            if(nombreUsuario!= null && contraseniaUsuario!= null){
                Usuario usuario = new Usuario();
                usuario.setNombreDeUsuario(nombreUsuario);
                usuario.setContrasenia(contraseniaUsuario);

                Rol rol = new Rol();
                rol.setTipo(TipoRol.PERSONA_HUMANA);
                rol.setPermisos(tipoPermisos);

                usuario.setRol(rol);
                colaborador.setUsuario(usuario);
            }

            try {
                beginTransaction();
                logger.info(String.format("Se creó el colaborador jurídico: %s", nombreUsuario));
                this.colaboradorRepository.guardar(colaborador);
                commitTransaction();

                context.redirect("/personaHumana");

            } catch (Exception e) {
                e.printStackTrace();
                context.status(500).result("Ha ocurrido un error inesperado.");
            }

        }

    }
}




