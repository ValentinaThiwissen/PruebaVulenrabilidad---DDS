package Domain.controller.ColaboradorController;

import Domain.models.entities.Ubicacion.Direccion;
import Domain.models.entities.Ubicacion.Localidad;
import Domain.models.entities.Ubicacion.Partido;
import Domain.models.entities.Ubicacion.Provincia;
import Domain.models.entities.Usuario.Rol;
import Domain.models.entities.Usuario.TipoPermiso;
import Domain.models.entities.Usuario.TipoRol;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.*;
import Domain.models.entities.formasDeColaborar.FormaDeColaboracion;
import Domain.models.entities.formasDeColaborar.TiposDeColaboracion;
import Domain.models.entities.tarjeta.TarjetaDeHeladeras;
import Domain.models.repository.ColaboradorRepository;
import Domain.models.repository.RolRepository;
import Domain.models.repository.UsuarioRepository;
import Domain.utils.ICrudViewsHandler;
import java.util.logging.Logger;


import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

import java.util.*;

public class ColaboradorJuridicoController implements ICrudViewsHandler, WithSimplePersistenceUnit {
    ColaboradorRepository colaboradorRepository;
    UsuarioRepository usuarioRepository;

    public ColaboradorJuridicoController(ColaboradorRepository colaboradorRepository, UsuarioRepository usuarioRepository) {
        this.colaboradorRepository = colaboradorRepository;
        this.usuarioRepository = usuarioRepository;
        //this.rolRepository = rolRepository;
    }

    private static final Logger logger = Logger.getLogger(ColaboradorJuridicoController.class.getName());

    @Override
    public void index(Context context) {
        //TODO
    }

    @Override
    public void show(Context context) {
        //TODO
    }

    @Override
    public void create(Context context) {
        context.render("registrarsePersonaJuridica.hbs");
    }

    public void createJuridica(Context context) {
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

        context.redirect("/personaJurica");


    }

    @Override
    public void save(Context context) {
        Colaborador colaborador = new Colaborador();
        colaborador.setTipoColaborador(TipoColaborador.PERSONAJURIDICA);

        colaborador.setNombre(context.formParam("nombre-organizacion"));
        colaborador.setRazonSocial(context.formParam("razonSocial"));

        String claseJuridica = context.formParam("tipoJuridico");
        System.out.print(claseJuridica);

        TipoJuridico tipoJuridico = TipoJuridico.valueOf(claseJuridica.toUpperCase());
        colaborador.setTipoJuridico(tipoJuridico);
        Set<String> formasDeColaborarSeleccionadas = new HashSet<>(context.formParams("formaColaborar[]"));
        Set<TiposDeColaboracion> formasDeColaborar = new HashSet<>();
        List<TipoPermiso> tipoPermisos = new ArrayList<>();
        for (String forma : formasDeColaborarSeleccionadas) {
            try {
                TiposDeColaboracion formaEnum = TiposDeColaboracion.valueOf(forma);
                formasDeColaborar.add(formaEnum);

                if(formaEnum.equals(TiposDeColaboracion.ENCARGARSEHELADERA)){
                    Set heladerasActivas = new HashSet<>();
                    colaborador.setHeladerasActivas(heladerasActivas);
                }
                else if (formaEnum.equals(TiposDeColaboracion.OFRECERPRODUCTOS)){
                    colaborador.setOfertasEmpresa(new HashSet<>());
                }

                TipoPermiso tipoPermiso = TipoPermiso.valueOf(forma);
                tipoPermisos.add(tipoPermiso);

            } catch (IllegalArgumentException e) {
                System.out.println("Forma de colaborar inválida: " + forma);
            }
        }
        colaborador.setFormasDisponiblesDeColaborar(formasDeColaborar);
        colaborador.setCanjesRealizados(new HashSet<>());

        colaborador.setPuntos(0.0);

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
            colaborador.setUnaDireccion(unDomicilio);
        }

        String nombreUsuario = context.formParam("usuario");
        String contraseniaUsuario = context.formParam("contrasenia");

        if(nombreUsuario!= null && contraseniaUsuario!= null){
            Usuario usuario = new Usuario();
            usuario.setNombreDeUsuario(nombreUsuario);
            usuario.setContrasenia(contraseniaUsuario);

            Rol rol = new Rol();
            rol.setTipo(TipoRol.PERSONA_JURIDICA);
            tipoPermisos.add(TipoPermiso.VISUALIZARREPORTES);
            rol.setPermisos(tipoPermisos);

            usuario.setRol(rol);
            colaborador.setUsuario(usuario);
        }

        try {
            beginTransaction();
            this.colaboradorRepository.guardar(colaborador);
            logger.info(String.format("Se creó el colaborador jurídico: %s", nombreUsuario));
            commitTransaction();

            context.redirect("/personaJuridica");

        } catch (Exception e) {
            e.printStackTrace();
            context.status(500).result("Ha ocurrido un error inesperado.");
        }
    }

    @Override
    public void edit(Context context) {
        //TODO
    }

    @Override
    public void update(Context context) {
        //TODO
    }

    @Override
    public void delete(Context context) {

    }
}