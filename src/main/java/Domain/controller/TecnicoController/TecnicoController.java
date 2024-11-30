package Domain.controller.TecnicoController;

import Domain.controller.HeladeraController.IncidenteController;
import Domain.models.entities.Ubicacion.*;
import Domain.models.entities.Usuario.Rol;
import Domain.models.entities.Usuario.TipoPermiso;
import Domain.models.entities.Usuario.TipoRol;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.colaborador.MedioDeContacto;
import Domain.models.entities.colaborador.TipoDeContacto;
import Domain.models.entities.formasDeColaborar.TiposDeColaboracion;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.Incidente;
import Domain.models.entities.personaVulnerable.TipoDocumento;
import Domain.models.entities.tarjeta.TarjetaDeHeladeras;
import Domain.models.entities.tarjeta.TarjetaDistribucionViandas;
import Domain.models.entities.tecnico.Tecnico;
import Domain.models.entities.tecnico.VisitaTecnica;
import Domain.models.repository.*;
import Domain.utils.ICrudViewsHandler;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TecnicoController  implements ICrudViewsHandler, WithSimplePersistenceUnit {

    UsuarioRepository usuarioRepository;
    RolRepository rolRepository;
    HeladeraRepository heladeraRepository;

    VisitasTecnicasRepository visitasTecnicasRepository;
    TecnicoRepository tecnicoRepository;
    private static final Logger logger = Logger.getLogger(TecnicoController.class.getName());

    public TecnicoController(VisitasTecnicasRepository visitasTecnicasRepository,HeladeraRepository heladeraRepository, UsuarioRepository usuarioRepository, RolRepository rolRepository,TecnicoRepository tecnicoRepository) {
        this.visitasTecnicasRepository = visitasTecnicasRepository;
        this.heladeraRepository = heladeraRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.tecnicoRepository = tecnicoRepository;

    }

    @Override
    public void index(Context context) {
    }

    @Override
    public void show(Context context) {

    }


    @Override
    public void create(Context context) {
        Long id = context.sessionAttribute("usuario_id");
        if (id == null) {
            context.redirect("/inicioSesion");
            return;
        }
        Usuario usuario = usuarioRepository.buscarPorID(id);
        if (usuario == null) {
            context.status(404).result("Usuario no encontrado");
            return;

        } else if (usuario.getRol().getTipo() == TipoRol.PERSONA_JURIDICA) {
            context.redirect("/personaJuridica");
        } else if (usuario.getRol().getTipo() == TipoRol.ADMIN) {
            context.redirect("/administrador");
        } else if (usuario.getRol().getTipo() == TipoRol.PERSONA_HUMANA) {
            context.redirect("/personaHumana");

        }
        else if(usuario.getRol().getTipo()==TipoRol.TECNICO) {
            Tecnico tecnico = tecnicoRepository.buscarPorUsuario(usuario);

            if (tecnico == null) {
                context.status(404).result("Tecnico no encontrado");
                return;
            }

            Map<String, Object> model = new HashMap<>();

            List<Heladera> heladeras = this.heladeraRepository.buscarPorEstado(false).stream()
                    .filter(heladera -> Geodecodificacion.estaDentroDelRadio(tecnico.getAreaDeCobertura().getPuntoGeografico(), heladera.getUnPuntoGeografico(), tecnico.getAreaDeCobertura().getRadioDeCobertura()))
                    .collect(Collectors.toList());

            model.put("heladeras", heladeras);
            context.render("visitaTecnica.hbs", model);
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

    @Override
    public void save(Context context) {
        Long id = context.sessionAttribute("usuario_id");
        if (id == null) {
            context.redirect("/inicioSesion");
            return;
        }
        Usuario usuario = usuarioRepository.buscarPorID(id);
        if (usuario == null) {
            context.status(404).result("Usuario no encontrado");
            return;
        } else if (usuario.getRol().getTipo() == TipoRol.TECNICO) {
            Tecnico tecnico = tecnicoRepository.buscarPorUsuario(usuario);

            if (tecnico == null) {
                context.status(404).result("Tecnico no encontrado");
                return;
            }

            VisitaTecnica visitaTecnica = new VisitaTecnica();
            visitaTecnica.setFechaVisita(LocalDate.parse(context.formParam("diaVisita")));

            visitaTecnica.setDescripcionTrabajo(context.formParam("descripcion"));
            String arreglado = context.formParam("arreglado");
            Long heladeraId = Long.valueOf(context.formParam("heladera"));
            Heladera heladera = this.heladeraRepository.buscarPorID(heladeraId);

            if (heladera == null) {
                context.status(213).result("Error no existe la heladera: " + heladeraId);
            } else {
                visitaTecnica.setHeladeraAArreglar(heladera);
            }

            if (arreglado.equals("SI")) {
                heladera.setEstaActiva(true);
                visitaTecnica.setSolucionadoTrabajo(true);
            } else if (arreglado.equals("NO")) {
                visitaTecnica.setSolucionadoTrabajo(false);
            }
                try {
                    Part foto = context.req().getPart("imagen");
                    if (foto != null && foto.getSize() > 0) {
                        String fileName = Paths.get(foto.getSubmittedFileName()).getFileName().toString();
                        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

                        if (!List.of("jpg", "jpeg", "png").contains(fileExtension)) {
                            context.status(400).result("Solo se permiten imágenes en formato JPG, JPEG o PNG.");
                            return;
                        }

                        if (foto.getSize() > 5 * 1024 * 1024) { // 5MB max
                            context.status(400).result("El archivo es demasiado grande. Máximo 5MB.");
                            return;
                        }

                        Path uploadDir = Paths.get("Entrega6", "src", "main", "resources", "public", "visitasTecnicas");
                        if (!Files.exists(uploadDir)) {
                            Files.createDirectories(uploadDir); // Crea el directorio si es necesario
                        }

                        String uniqueFileName = System.currentTimeMillis() + "_" + fileName; /*Nombre unico*/

                        // Construir ruta completa del archivo
                        Path filePath = uploadDir.resolve(uniqueFileName);

                        // Guardar el archivo
                        Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                        // Actualizar la entidad con la ruta relativa del archivo
                        visitaTecnica.setFotoTrabajo("/visitasTecnicas/" + uniqueFileName);
                  }

                    beginTransaction();
                    this.visitasTecnicasRepository.guardar(visitaTecnica);
                    logger.info("Se guardo la visita tecnica");
                    commitTransaction();
                    Map<String, Object> model = new HashMap<>();

                    model.put("solicitudExitosa", true);
                    model.put("visitaRegistrada", "¡Se registro tu visita tecnica!");
                    context.render("visitaTecnica.hbs", model);

                } catch (IOException | ServletException e) {

                    e.printStackTrace();
                    context.status(500).result("Error al procesar el formulario o guardarCanje el archivo.");
                }
            } else if (usuario.getRol().getTipo() == TipoRol.PERSONA_JURIDICA) {
                context.redirect("/personaJuridica");
            } else if (usuario.getRol().getTipo() == TipoRol.ADMIN) {
                context.redirect("/administrador");
            } else if (usuario.getRol().getTipo() == TipoRol.PERSONA_HUMANA) {
                context.redirect("/personaHumana");
            }
    }

    public void createRegistrarTecnico(Context context){
        /*Long id = context.sessionAttribute("usuario_id");
        if (id == null) {
            context.redirect("/inicioSesion");
            return;
        }
        Usuario usuario = usuarioRepository.buscarPorID(id);
        if (usuario == null) {
            context.status(404).result("Usuario no encontrado");
            return;
        }
        if (usuario.getRol().getTipo() == TipoRol.ADMIN && usuario.getRol().getPermisos().contains(TipoPermiso.REGISTRARTECNICO))
        {*/
        List<Heladera> heladeras = heladeraRepository.buscarTodos();

        Map<String, Object> model = new HashMap<>();
        model.put("heladeras", heladeras);
        context.render("tecnico2.hbs", model);
    }

    public void saveTecnico(Context context) {
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
        Tecnico tecnico = new Tecnico();
        tecnico.setNombre(context.formParam("nombre"));
        tecnico.setApellido(context.formParam("apellido"));

        tecnico.setCuil(Long.valueOf(context.formParam("cuil")));

        String tipoDocumento = context.formParam("tipo_documento");
        if (tipoDocumento.equalsIgnoreCase("DNI")) {
            tecnico.setTipoDocumento(TipoDocumento.DNI);
        } else if (tipoDocumento.equalsIgnoreCase("LIBRETAENROLAMIENTO")) {
            tecnico.setTipoDocumento(TipoDocumento.LIBRETAENROLAMIENTO);
        } else if (tipoDocumento.equalsIgnoreCase("LIBRETACIVICA")) {
            tecnico.setTipoDocumento(TipoDocumento.LIBRETACIVICA);
        }
        try {
            int nroDocumento = Integer.parseInt(context.formParam("documento"));
            tecnico.setNroDocumento(nroDocumento);
        } catch (NumberFormatException e) {
            context.status(400).result("Documento inválido");
            return;
        }

        tecnico.setMail(context.formParam("mail"));


        List<String> contactos = context.formParams("contacto[]");
        List<String> tiposContacto = context.formParams("tipo_contacto[]");

        if (contactos != null && tiposContacto != null && contactos.size() == tiposContacto.size()) {
            if (tecnico.getMedio() == null) {
                tecnico.setMedio(new HashSet<>());
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
                        tecnico.getMedio().add(medioDeContacto);

                    } catch (IllegalArgumentException e) {
                        System.out.println("Tipo de contacto no válido: " + tipoContacto);
                    }
                }
            }
        }

        if (tecnico.getVisitas() == null) {
            tecnico.setVisitas(new HashSet<>());
        }

        Set<String> heladerasSeleccionadas = new HashSet<>(context.formParams("heladerasSeleccionadas[]"));

        String altura = context.formParam("altura");
        String calle = context.formParam("calle");
        String localidad = context.formParam("localidad");
        String partido_nombre = context.formParam("partido");
        String nombre_provincia = context.formParam("provincia");
        String radio = context.formParam("radio");

        if ((altura != null && !altura.isEmpty() && calle != null && !calle.isEmpty()
                && localidad != null && !localidad.isEmpty()
                && partido_nombre != null && !partido_nombre.isEmpty()
                && nombre_provincia != null && !nombre_provincia.isEmpty())) {

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

            PuntoGeografico puntoGeografico = Geodecodificacion.obtenerCoordenadas(calle, altura, localidad, nombre_provincia);
            if (puntoGeografico.getLatitud() == null && puntoGeografico.getLongitud() == null ) {
                context.status(500).result("Error al encontrar punto geografico.");
            } else {
                puntoGeografico.setDireccion(unDomicilio);

                AreaDeCobertura area = new AreaDeCobertura();
                area.setPuntoGeografico(puntoGeografico);
                area.setRadioDeCobertura(Integer.valueOf(radio));

                tecnico.setAreaDeCobertura(area);
            }
        }

        Usuario usuarioTecnico = new Usuario();
        String nombreUsuario = context.formParam("usuario");
        String contraseniaUsuario = context.formParam("contrasenia");
        if(nombreUsuario!= null && contraseniaUsuario!= null){
            usuarioTecnico.setNombreDeUsuario(nombreUsuario);

            usuarioTecnico.setContrasenia(contraseniaUsuario);

            Rol rolTecnico = new Rol();
            List<TipoPermiso> listaPermisos = new ArrayList<>();
            listaPermisos.add(TipoPermiso.REGISTROVISITATECNICA);
            rolTecnico.setPermisos(listaPermisos);
            rolTecnico.setTipo(TipoRol.TECNICO);
            usuarioTecnico.setRol(rolTecnico);
            tecnico.setUsuario(usuarioTecnico);}

        try{
            beginTransaction();
            this.tecnicoRepository.guardar(tecnico);
            logger.info(String.format("Se guardo el incidente, su descripcion %s", nombreUsuario));
            commitTransaction();

            Map<String, Object> model = new HashMap<>();
            model.put("solicitudExitosa", true);
            model.put("tecnicoResgistrado", "¡Se registro el técnico correctamente!");
            context.render("tecnico2.hbs", model);

        } catch (Exception e) {
            e.printStackTrace();
            context.status(500).result("Error al procesar el formulario o guardar técnico.");
        }
    }
}

