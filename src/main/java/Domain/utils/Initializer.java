package Domain.utils;

import Domain.models.entities.Ubicacion.AreaDeCobertura;
import Domain.models.entities.Ubicacion.Direccion;
import Domain.models.entities.Ubicacion.PuntoGeografico;
import Domain.models.entities.Usuario.Rol;
import Domain.models.entities.Usuario.TipoPermiso;
import Domain.models.entities.Usuario.TipoRol;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.*;
import Domain.models.entities.formasDeColaborar.*;
import Domain.models.entities.formulario.*;
import Domain.models.entities.heladera.*;
import Domain.models.entities.personaVulnerable.PersonaVulnerable;
import Domain.models.entities.personaVulnerable.TipoDocumento;
import Domain.models.entities.tarjeta.SolicitudApertura;
import Domain.models.entities.tarjeta.TarjetaDeHeladeras;
import Domain.models.entities.tarjeta.TarjetaDistribucionViandas;
import Domain.models.entities.tecnico.Tecnico;
import Domain.models.entities.tecnico.VisitaTecnica;
import Domain.models.entities.vianda.Comida;
import Domain.models.entities.vianda.Vianda;
import Domain.models.repository.*;
import Domain.models.suscripciones.AgregarViandas;
import Domain.models.suscripciones.Suscripcion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Initializer implements WithSimplePersistenceUnit {

    private HeladeraRepository heladeraRepository;
    private ColaboradorRepository colaboradorRepository;
    private TarjetaHeladerasRepository tarjetaHeladerasRepository;
    private PersonaVulnerableRepository personaVulnerableRepository;
    private TecnicoRepository tecnicoRepository;
    private IncidenteRepository incidenteRepository;
    private FormaColaboracionRepository colaboracionRepository;
    private FormularioRepository  formularioRepository;
    private SuscripcionesRepository suscripcionesRepository;
    private CanjeRepository canjeRepository;
    private ViandaRepository viandaRepository;
    private FormularioRespondidoRepository formularioRespondidoRepository;
    private OfertaRepository ofertaRepository;
    private SolicitudAperturaRepository solicitudAperturaRepository;
    private UsuarioRepository usuarioRepository;
    private ModeloRepository modeloRepository;

    private Heladera heladera1;
    private Heladera heladera2;
    private Heladera heladera3;
    private Heladera heladera4;

    private Vianda viandaRaviol;
    private Formulario formulario;
    private OpcionRespuesta opcionRespuesta1;
    private Pregunta pregunta1;

    private PuntoGeografico puntoFlores;

    private TarjetaDistribucionViandas tarjetaDistribucionViandas1;

    private TarjetaDeHeladeras tarjetaDeHeladeras1;
    private TarjetaDeHeladeras tarjetaDeHeladeras2;
    private TarjetaDeHeladeras tarjetaDeHeladeras3;

    private Colaborador cata;
    private Colaborador valen;
    private Colaborador juampi;
    private Colaborador mcdonalds;

    public static void init() {
        Initializer instance = new Initializer();
        instance.heladeraRepository = new HeladeraRepository();
        instance.colaboradorRepository = new ColaboradorRepository();
        // instance.tarjetasDistriRepository = new TarjetaDistribucionViandaRepository();
        instance.tarjetaHeladerasRepository = new TarjetaHeladerasRepository();
        instance.personaVulnerableRepository = new PersonaVulnerableRepository();
        instance.tecnicoRepository = new TecnicoRepository();
        instance.incidenteRepository= new IncidenteRepository();
        instance.canjeRepository = new CanjeRepository();
        instance.viandaRepository = new ViandaRepository();
        instance.colaboracionRepository = new FormaColaboracionRepository();
        instance.suscripcionesRepository = new SuscripcionesRepository();
        instance.formularioRepository = new FormularioRepository();
        instance.formularioRespondidoRepository = new FormularioRespondidoRepository();
        instance.ofertaRepository = new OfertaRepository();
        instance.solicitudAperturaRepository= new SolicitudAperturaRepository();
        instance.usuarioRepository = new UsuarioRepository();
        instance.modeloRepository = new ModeloRepository();

        instance.guardarHeladeras();
        instance.guardarAdministrador();
        instance.guardarModelos();

        instance.guardarFormularioHumano();
        instance.guardarFormularioJuridico();
        instance.guardarFormularioPersonaVulnerable();
        instance.guardarFormularioDonacionMonetaria();
        instance.guardarFormularioDonacionVianda();
        instance.guardarFormularioDistribuirViandas();
        instance.guardarFormularioEncargoHeladera();
        instance.guardarFormularioReportar();
        instance.guardarFormularioOfrecerProductos();
        instance.guardarFormularioRegistropApertura();
        instance.guardarFormulairoSuscripcionCaso1();
        instance.guardarFormularioSuscripcionCaso2();
        instance.guardarFormularioSuscripcionCaso3();

        instance.guardarCanjes();
    }

    private void guardarAdministrador() {
        Usuario usuario = new Usuario();
        usuario.setNombreDeUsuario("administrador");
        usuario.setContrasenia("administrador");

        Rol rol = new Rol();
        rol.setTipo(TipoRol.ADMIN);
        List<TipoPermiso> permisos = new ArrayList<>();
        permisos.add(TipoPermiso.CARGACSV);
        permisos.add(TipoPermiso.VISUALIZARALERTAS);
        permisos.add(TipoPermiso.VISUALIZARREPORTES);
        permisos.add(TipoPermiso.VISUALIZARHELADERAS);
        permisos.add(TipoPermiso.GESTIONARFORMULARIOS);
        permisos.add(TipoPermiso.GESTIONARUSUARIOS);
        permisos.add(TipoPermiso.REGISTRARTECNICO);
        permisos.add(TipoPermiso.VISUALIZARVISITASTECNICAS);
        rol.setPermisos(permisos);

        usuario.setRol(rol);

        usuarioRepository.actualizar2(usuario);
    }

    private void guardarModelos(){

        Modelo modelo1  = new Modelo("Samsung", 1, 13);
        Modelo modelo2  = new Modelo("Ford", 45, 78);
        Modelo modelo3  = new Modelo("Audi", 21, 50);
        beginTransaction();
        modeloRepository.actualizar2(modelo1);
        modeloRepository.actualizar2(modelo2);
        modeloRepository.actualizar2(modelo3);
        commitTransaction();
    }

    private void guardarHeladeras() {
        beginTransaction();

        PuntoGeografico puntoParqueChacabuco = new PuntoGeografico(-34.63729187924965, -58.43969407345252);
        Modelo electrolux = new Modelo("Electrolux", 1,4);
        heladera1 = new Heladera();
        heladera1.setNombreHeladera("HeladeraParqueChacabuco");
        heladera1.setCapacidad(8);
        heladera1.setUnModelo(electrolux);
        heladera1.setFechaActivacion(LocalDate.now());
        heladera1.setUnPuntoGeografico(puntoParqueChacabuco);
        heladera1.setEstaActiva(true);
        heladera1.setTiempoPermitidoAperturaMinutos(80);

        puntoFlores = new PuntoGeografico(-34.63595601527638, -58.46060666417879);
        Modelo drean = new Modelo("Drean", 2,5);
        heladera2 = new Heladera();
        heladera2.setNombreHeladera("HeladeraFlores");
        heladera2.setCapacidad(90);
        heladera2.setUnModelo(drean);
        heladera2.setFechaActivacion(LocalDate.of(2010, 6, 16));
        heladera2.setUnPuntoGeografico(puntoFlores);
        heladera2.setEstaActiva(true);
        heladera2.setTiempoPermitidoAperturaMinutos(60);

        PuntoGeografico puntoTanti = new PuntoGeografico(-31.362742857639176, -64.58266812237707);
        Modelo philco = new Modelo("Philco", 1,5);
        heladera3 = new Heladera();
        heladera3.setNombreHeladera("HeladeraCordoba");
        heladera3.setCapacidad(100);
        heladera3.setUnModelo(philco);
        heladera3.setFechaActivacion(LocalDate.of(2012, 9, 10));
        heladera3.setUnPuntoGeografico(puntoTanti);
        heladera3.setEstaActiva(true);
        heladera3.setTiempoPermitidoAperturaMinutos(80);

        PuntoGeografico puntoEsperanza = new PuntoGeografico(-36.64454640666693, -64.26039564145117);
        Modelo samsung = new Modelo("Samsung", 2,6);
        heladera4 = new Heladera();
        heladera4.setNombreHeladera("HeladeraEsperanza");
        heladera4.setCapacidad(120);
        heladera4.setUnModelo(samsung);
        heladera4.setFechaActivacion(LocalDate.of(2016, 12, 26));
        heladera4.setUnPuntoGeografico(puntoEsperanza);
        heladera4.setEstaActiva(true);
        heladera4.setTiempoPermitidoAperturaMinutos(40);


        heladeraRepository.actualizar(heladera1);
        heladeraRepository.actualizar(heladera2);
        heladeraRepository.actualizar(heladera3);
        heladeraRepository.actualizar(heladera4);
        commitTransaction();
    }

    private void guardarFormularioHumano(){
        withTransaction(()->{
            Formulario formularioRegistroPH = new Formulario();
            formularioRegistroPH.setNombreForm("RegistroPersonaHumana");

            Pregunta preguntaNombre = new Pregunta("nombre", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaApellido = new Pregunta("apellido", true, TipoPregunta.DESARROLLO);
            OpcionRespuesta dni = new OpcionRespuesta("DNI");
            OpcionRespuesta libretaEnrolamiento = new OpcionRespuesta("LIBRETAENROLAMIENTO");
            OpcionRespuesta libretaCivica = new OpcionRespuesta("LIBRETACIVICA");
            List<OpcionRespuesta> tiposDocumento = new ArrayList<>();
            tiposDocumento.add(dni);
            tiposDocumento.add(libretaEnrolamiento);
            tiposDocumento.add(libretaCivica);
            Pregunta preguntaTipoDNI = new Pregunta("tipo_documento", true, TipoPregunta.OPCION_UNICA, tiposDocumento);
            Pregunta preguntaDoc = new Pregunta("documento", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaMail = new Pregunta("mail", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaNacimiento = new Pregunta("nacimiento", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaLocalidad = new Pregunta("localidad", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaPartido = new Pregunta("partido", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaProvincia = new Pregunta("provincia", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaAltura = new Pregunta("altura", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaCalle = new Pregunta("calle", true, TipoPregunta.DESARROLLO);
            OpcionRespuesta whatsapp = new OpcionRespuesta("Whatsapp");
            OpcionRespuesta telefono = new OpcionRespuesta("Telefono");
            OpcionRespuesta telegram = new OpcionRespuesta("Telegram");
            OpcionRespuesta mail = new OpcionRespuesta("Mail");
            List<OpcionRespuesta> tipoContacto = new ArrayList<>();
            tipoContacto.add(whatsapp);
            tipoContacto.add(telegram);
            tipoContacto.add(telefono);
            tipoContacto.add(mail);
            Pregunta preguntaTipoContacto = new Pregunta("tipo_contacto", true, TipoPregunta.OPCION_MULTIPLE, tipoContacto);
            Pregunta preguntaContacto = new Pregunta("contacto", true, TipoPregunta.DESARROLLO);
            OpcionRespuesta donacionMonetaria = new OpcionRespuesta("DONACIONMONETARIA");
            OpcionRespuesta distribucionVianda = new OpcionRespuesta("DISTRIBUCIONVIANDA");
            OpcionRespuesta donacionvianda = new OpcionRespuesta("DONACIONVIANDA");
            OpcionRespuesta registrarpersonavulnerable = new OpcionRespuesta("REGISTRARPERSONAVULNERABLE");
            List<OpcionRespuesta> formaColaborar = new ArrayList<>();
            formaColaborar.add(donacionvianda);
            formaColaborar.add(distribucionVianda);
            formaColaborar.add(donacionMonetaria);
            formaColaborar.add(registrarpersonavulnerable);
            Pregunta preguntaFormaColaborar = new Pregunta("formaColaborar", true, TipoPregunta.OPCION_MULTIPLE, formaColaborar);
            Pregunta preguntaUsuario = new Pregunta("usuario", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaContrasenia = new Pregunta("contrasenia", true, TipoPregunta.DESARROLLO);
        
            List<Pregunta> preguntas = new ArrayList<>();
            preguntas.add(preguntaNombre);
            preguntas.add(preguntaApellido);
            preguntas.add(preguntaTipoDNI);
            preguntas.add(preguntaDoc);
            preguntas.add(preguntaTipoContacto);
            preguntas.add(preguntaContacto);
            preguntas.add(preguntaLocalidad);
            preguntas.add(preguntaPartido);
            preguntas.add(preguntaProvincia);
            preguntas.add(preguntaAltura);
            preguntas.add(preguntaCalle);
            preguntas.add(preguntaMail);
            preguntas.add(preguntaUsuario);
            preguntas.add(preguntaContrasenia);
            preguntas.add(preguntaNacimiento);
            preguntas.add(preguntaFormaColaborar);

            formularioRegistroPH.setPreguntas(preguntas);

            formularioRepository.actualizar2(formularioRegistroPH);
        });

    }
    private void guardarFormularioJuridico(){
        withTransaction(()->{
            Formulario formularioRegistroPJ = new Formulario();
            formularioRegistroPJ.setNombreForm("RegistroPersonaJuridica");

            Pregunta preguntaNombre = new Pregunta("nombre", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaApellido = new Pregunta("razon social", true, TipoPregunta.DESARROLLO);

            OpcionRespuesta gubernamental = new OpcionRespuesta("GUBERNAMENTAL");
            OpcionRespuesta ong = new OpcionRespuesta("ONG");
            OpcionRespuesta empresa = new OpcionRespuesta("EMPRESA");
            OpcionRespuesta institucional = new OpcionRespuesta("INSTITUCIONAL");
            List<OpcionRespuesta> tipoJuridico = new ArrayList<>();
            tipoJuridico.add(gubernamental);
            tipoJuridico.add(ong);
            tipoJuridico.add(empresa);
            tipoJuridico.add(institucional);
            Pregunta preguntaTipoJuridico = new Pregunta("tipoJuridico", true, TipoPregunta.OPCION_UNICA,tipoJuridico);

            OpcionRespuesta donacionmonetaria = new OpcionRespuesta("DONACIONMONETARIA");
            OpcionRespuesta encargarseheladera = new OpcionRespuesta("ENCARGARSEHELADERA");
            OpcionRespuesta ofrecerproductos = new OpcionRespuesta("OFRECERPRODUCTOS");
            List<OpcionRespuesta> formasDeColaborar = new ArrayList<>();
            formasDeColaborar.add(donacionmonetaria);
            formasDeColaborar.add(encargarseheladera);
            formasDeColaborar.add(ofrecerproductos);
            Pregunta formaColaborar = new Pregunta("formaColaborar", true, TipoPregunta.OPCION_MULTIPLE, formasDeColaborar);

            OpcionRespuesta whatsapp = new OpcionRespuesta("Whatsapp");
            OpcionRespuesta telefono = new OpcionRespuesta("Telefono");
            OpcionRespuesta telegram = new OpcionRespuesta("Telegram");
            OpcionRespuesta mail = new OpcionRespuesta("Mail");
            List<OpcionRespuesta> tipoContacto = new ArrayList<>();
            tipoContacto.add(whatsapp);
            tipoContacto.add(telegram);
            tipoContacto.add(telefono);
            tipoContacto.add(mail);
            Pregunta preguntaTipoContacto = new Pregunta("tipo_contacto", true, TipoPregunta.OPCION_MULTIPLE, tipoContacto);
            Pregunta preguntaContacto = new Pregunta("contacto", true, TipoPregunta.DESARROLLO);

            Pregunta preguntaLocalidad = new Pregunta("localidad", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaPartido = new Pregunta("partido", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaProvincia = new Pregunta("provincia", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaAltura = new Pregunta("altura", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaCalle = new Pregunta("calle", true, TipoPregunta.DESARROLLO);

            Pregunta preguntaMail = new Pregunta("mail", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaUsuario = new Pregunta("usuario", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaContrasenia = new Pregunta("mail", true, TipoPregunta.DESARROLLO);

            ArrayList<Pregunta> preguntas = new ArrayList<>();
            preguntas.add(preguntaNombre);
            preguntas.add(preguntaApellido);
            preguntas.add(preguntaTipoJuridico);
            preguntas.add(preguntaTipoContacto);
            preguntas.add(preguntaContacto);
            preguntas.add(preguntaLocalidad);
            preguntas.add(preguntaPartido);
            preguntas.add(preguntaProvincia);
            preguntas.add(preguntaAltura);
            preguntas.add(preguntaCalle);
            preguntas.add(preguntaMail);
            preguntas.add(preguntaUsuario);
            preguntas.add(preguntaContrasenia);

            formularioRegistroPJ.setPreguntas(preguntas);
            formularioRepository.actualizar2(formularioRegistroPJ);
        });

    }
    private void guardarFormularioPersonaVulnerable(){
        withTransaction(()-> {
            Formulario formularioRegistroPV = new Formulario();
            formularioRegistroPV.setNombreForm("RegistroPersonaVulnerable");
            Pregunta preguntaNombre = new Pregunta("nombre", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaApellido = new Pregunta("apellido", true, TipoPregunta.DESARROLLO);

            OpcionRespuesta dni = new OpcionRespuesta("DNI");
            OpcionRespuesta libretaEnrolamiento = new OpcionRespuesta("LIBRETAENROLAMIENTO");
            OpcionRespuesta libretaCivica = new OpcionRespuesta("LIBRETACIVICA");
            List<OpcionRespuesta> tiposDocumento = new ArrayList<>();
            tiposDocumento.add(dni);
            tiposDocumento.add(libretaEnrolamiento);
            tiposDocumento.add(libretaCivica);
            Pregunta preguntaTipoDNI = new Pregunta("tipo_documento", true, TipoPregunta.OPCION_UNICA, tiposDocumento);
            Pregunta preguntaDoc = new Pregunta("documento", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaNacimiento = new Pregunta("nacimiento", true, TipoPregunta.DESARROLLO);

            Pregunta preguntaLocalidad = new Pregunta("localidad", false, TipoPregunta.DESARROLLO);
            Pregunta preguntaPartido = new Pregunta("partido", false, TipoPregunta.DESARROLLO);
            Pregunta preguntaProvincia = new Pregunta("provincia", false, TipoPregunta.DESARROLLO);
            Pregunta preguntaAltura = new Pregunta("altura", false, TipoPregunta.DESARROLLO);
            Pregunta preguntaCalle = new Pregunta("calle", false, TipoPregunta.DESARROLLO);

            OpcionRespuesta si = new OpcionRespuesta("SI");
            OpcionRespuesta no = new OpcionRespuesta("NO");
            List<OpcionRespuesta> menores = new ArrayList<>();
            menores.add(si);
            menores.add(no);
            Pregunta menoresACargo = new Pregunta("menoresACargo", true, TipoPregunta.OPCION_UNICA);

            formularioRepository.actualizar2(formularioRegistroPV);
        });

    }
    public void guardarFormularioDonacionMonetaria(){
        withTransaction(()-> {
            Formulario formularioDonacionMonetaria = new Formulario();
            formularioDonacionMonetaria.setNombreForm("Formulario Donacion Mo");
            Pregunta monto= new Pregunta("monto-donacion", true, TipoPregunta.OPCION_UNICA);
            OpcionRespuesta mill = new OpcionRespuesta("1000");
            OpcionRespuesta cincomill = new OpcionRespuesta("5000");
            OpcionRespuesta diezmil = new OpcionRespuesta("10000");
            OpcionRespuesta cincuentamill = new OpcionRespuesta("50000");
            OpcionRespuesta cienmil = new OpcionRespuesta("100000");
            List<OpcionRespuesta> tipoDonaciones = new ArrayList<>();
            tipoDonaciones.add(mill);
            tipoDonaciones.add(cincomill);
            tipoDonaciones.add(cienmil);
            tipoDonaciones.add(diezmil);
            Pregunta frecuencia= new Pregunta("frecuencia", true, TipoPregunta.OPCION_UNICA);
            OpcionRespuesta SEMANALMENTE = new OpcionRespuesta("SEMANALMENTE");
            OpcionRespuesta MENSUALMENTE = new OpcionRespuesta("MENSUALMENTE");
            OpcionRespuesta ANUALMENTE = new OpcionRespuesta("ANUALMENTE");
            List<OpcionRespuesta> tiposFrecuencia = new ArrayList<>();
            tiposFrecuencia.add(SEMANALMENTE);
            tiposFrecuencia.add(MENSUALMENTE);
            tiposFrecuencia.add(ANUALMENTE);
            Pregunta repeticion= new Pregunta("repeticion", true, TipoPregunta.DESARROLLO);
            Pregunta medioPago= new Pregunta("medioPago", true, TipoPregunta.OPCION_UNICA);
            Pregunta numeroTarjeta= new Pregunta("numeroTarjeta", true, TipoPregunta.DESARROLLO);
            List<OpcionRespuesta>TipoPagos = new ArrayList<>();
            OpcionRespuesta debito = new OpcionRespuesta("debito");
            OpcionRespuesta credito = new OpcionRespuesta("credito");
            TipoPagos.add(credito);
            TipoPagos.add(debito);
            tiposFrecuencia.add(ANUALMENTE);
            ArrayList<Pregunta> preguntas = new ArrayList<>();
            preguntas.add(medioPago);
            preguntas.add(repeticion);
            preguntas.add(frecuencia);
            preguntas.add(monto);
            preguntas.add(numeroTarjeta);
            formularioDonacionMonetaria.setPreguntas(preguntas);

            formularioRepository.actualizar2(formularioDonacionMonetaria);
        });
    }
    private void guardarFormularioDistribuirViandas(){
        withTransaction(()-> {
            Formulario formularioRegistroDistribucion = new Formulario();
            formularioRegistroDistribucion.setNombreForm("Formulario Distribucion Viandas");
            Pregunta preguntaHeladera = new Pregunta("heladera", true, TipoPregunta.DESARROLLO);
            OpcionRespuesta uno = new OpcionRespuesta("1");
            OpcionRespuesta dos = new OpcionRespuesta("2");
            OpcionRespuesta tres = new OpcionRespuesta("3");
            OpcionRespuesta cuatro = new OpcionRespuesta("4");
            OpcionRespuesta cinco = new OpcionRespuesta("5");
            List<OpcionRespuesta> cantidad = new ArrayList<>();
            cantidad.add(uno);
            cantidad.add(dos);
            cantidad.add(tres);
            cantidad.add(cinco);
            cantidad.add(cuatro);
            Pregunta preguntaCantidad = new Pregunta("cantidad", true, TipoPregunta.OPCION_UNICA, cantidad);
            OpcionRespuesta desperfecto = new OpcionRespuesta("DESPERFECTO_HELADERA");
            OpcionRespuesta falta = new OpcionRespuesta("FALTA_VIANDAS");
            OpcionRespuesta sobran = new OpcionRespuesta("SOBRAN_VIANDAS");
            List<OpcionRespuesta> motivo = new ArrayList<>();
            motivo.add(desperfecto);
            motivo.add(falta);
            motivo.add(sobran);
            Pregunta preguntaMotivo = new Pregunta("motivo", true, TipoPregunta.OPCION_UNICA, motivo);

            List<Pregunta> preguntas = new ArrayList<>();

            preguntas.add(preguntaHeladera);
            preguntas.add(preguntaCantidad);
            preguntas.add(preguntaMotivo);

            formularioRegistroDistribucion.setPreguntas(preguntas);
            formularioRepository.actualizar2(formularioRegistroDistribucion);

        });

    }
    public void guardarFormularioDonacionVianda(){
        withTransaction(()-> {

            Formulario formularioDonacionVianda = new Formulario();
            formularioDonacionVianda.setNombreForm("Formulario Donacion Vianda");

            OpcionRespuesta uno = new OpcionRespuesta("1");
            OpcionRespuesta dos = new OpcionRespuesta("2");
            OpcionRespuesta tres = new OpcionRespuesta("3");
            OpcionRespuesta cinco = new OpcionRespuesta("5");
            OpcionRespuesta diez = new OpcionRespuesta("10");
            OpcionRespuesta veinte = new OpcionRespuesta("20");
            List<OpcionRespuesta> cantidad = new ArrayList<>();
            cantidad.add(uno);
            cantidad.add(dos);
            cantidad.add(tres);
            cantidad.add(cinco);
            cantidad.add(diez);
            cantidad.add(veinte);

            Pregunta preguntaCantidad = new Pregunta("Cantidad de viandas a donar", true,TipoPregunta.OPCION_UNICA, cantidad);
            Pregunta preguntaContenido = new Pregunta("Contenido vianda", true,TipoPregunta.DESARROLLO);
            Pregunta preguntaPeso = new Pregunta("Peso vianda", false,TipoPregunta.DESARROLLO);
            Pregunta preguntaCaloria = new Pregunta("Caloria vianda", false,TipoPregunta.DESARROLLO);
            Pregunta preguntaFechaCaducidad = new Pregunta("Fecha Caducidad", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaHeladera = new Pregunta("Heladera destino", true, TipoPregunta.DESARROLLO);

            List<Pregunta> preguntas = new ArrayList<>();
            preguntas.add(preguntaCantidad);
            preguntas.add(preguntaContenido);
            preguntas.add(preguntaPeso);
            preguntas.add(preguntaCaloria);
            preguntas.add(preguntaFechaCaducidad);
            preguntas.add(preguntaHeladera);
            formularioDonacionVianda.setPreguntas(preguntas);
            formularioRepository.actualizar2(formularioDonacionVianda);

        });
    }
    private void guardarFormularioEncargoHeladera(){
        withTransaction(()-> {
            Formulario formularioEncargoHeladera = new Formulario();
            formularioEncargoHeladera.setNombreForm("Formulario encargo de heladera");

            Pregunta preguntaHeladera = new Pregunta("heladera", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaModelo = new Pregunta("modelo", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaMinima = new Pregunta("minima", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaMaxima = new Pregunta("maxima", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaCapacidad = new Pregunta("capacidad", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaLocalidad = new Pregunta("localidad", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaPartido = new Pregunta("partido", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaProvincia = new Pregunta("provincia", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaAltura = new Pregunta("altura", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaCalle = new Pregunta("calle", true, TipoPregunta.DESARROLLO);

            List<Pregunta> preguntas = new ArrayList<>();
            preguntas.add(preguntaHeladera);
            preguntas.add(preguntaModelo);
            preguntas.add(preguntaMinima);
            preguntas.add(preguntaMaxima);
            preguntas.add(preguntaCapacidad);
            preguntas.add(preguntaLocalidad);
            preguntas.add(preguntaPartido);
            preguntas.add(preguntaProvincia);
            preguntas.add(preguntaAltura);
            preguntas.add(preguntaCalle);

            formularioEncargoHeladera.setPreguntas(preguntas);
            formularioRepository.actualizar2(formularioEncargoHeladera);
        });

    }
    private void guardarFormularioOfrecerProductos(){
        withTransaction(()-> {
            Formulario formulacionOfrecerProductos= new Formulario();
            formulacionOfrecerProductos.setNombreForm("Formulario de ofrecerProductos");
            Pregunta preguntaNombre = new Pregunta("Nombre heladera", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaPuntos = new Pregunta("Puntos heladera", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaProductoOfrece = new Pregunta("Producto a ofrecer", true, TipoPregunta.DESARROLLO);

            OpcionRespuesta uno = new OpcionRespuesta("Tecnología");
            OpcionRespuesta dos = new OpcionRespuesta("Alimentación");
            OpcionRespuesta tres = new OpcionRespuesta("Ropa");
            OpcionRespuesta cuatro = new OpcionRespuesta("Librería");
            OpcionRespuesta cinco = new OpcionRespuesta("Otros");

            List<OpcionRespuesta> cantidad = new ArrayList<>();
            cantidad.add(uno);
            cantidad.add(dos);
            cantidad.add(tres);
            cantidad.add(cinco);
            cantidad.add(cuatro);

            Pregunta preguntaRubro= new Pregunta("Rubro", true, TipoPregunta.OPCION_UNICA, cantidad);
            Pregunta preguntaImagen= new Pregunta("Imagen Oferta", true, TipoPregunta.DESARROLLO);


            List<Pregunta> preguntas = new ArrayList<>();
            preguntas.add(preguntaNombre);
            preguntas.add(preguntaPuntos);
            preguntas.add(preguntaProductoOfrece);
            preguntas.add(preguntaRubro);
            preguntas.add(preguntaImagen);
            formulacionOfrecerProductos.setPreguntas(preguntas);

            formularioRepository.actualizar2(formulacionOfrecerProductos);
        });
    
    }
    public void guardarFormularioReportar(){
        withTransaction(()-> {
            Formulario formularioReportar= new Formulario();

            Pregunta heladera = new Pregunta("heladera", true, TipoPregunta.DESARROLLO);

            Pregunta observaciones= new Pregunta("Observaciones", false, TipoPregunta.DESARROLLO);
            Pregunta imagen = new Pregunta("imagen", false, TipoPregunta.DESARROLLO);
            List<Pregunta> preguntas = new ArrayList<>();
            preguntas.add(imagen);
            preguntas.add(observaciones);
            preguntas.add(heladera);
            formularioReportar.setPreguntas(preguntas);

            formularioRepository.actualizar2(formularioReportar);
        });
    }
    public void guardarFormularioRegistropApertura(){
        withTransaction(()-> {
            Formulario formulacionOfrecerProductos= new Formulario();
            formulacionOfrecerProductos.setNombreForm("Formulario Registro Apertura");

            OpcionRespuesta uno = new OpcionRespuesta("Ingresar Viandas");
            OpcionRespuesta dos = new OpcionRespuesta("Retirar Viandas");

            List<OpcionRespuesta> opciones = new ArrayList<>();
            opciones.add(uno);
            opciones.add(dos);

            Pregunta preguntaMotivo = new Pregunta("Motivo heladera", true, TipoPregunta.OPCION_UNICA, opciones);
            Pregunta preguntaHeladera = new Pregunta("Nombre heladera", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaCantidadViandas = new Pregunta("Cantidad Viandas", true, TipoPregunta.DESARROLLO);


            List<Pregunta> preguntas = new ArrayList<>();
            preguntas.add(preguntaMotivo);
            preguntas.add(preguntaHeladera);
            preguntas.add(preguntaCantidadViandas);
            formulacionOfrecerProductos.setPreguntas(preguntas);
            formularioRepository.actualizar2(formulacionOfrecerProductos);
        });
    }
    
    public void guardarFormulairoSuscripcionCaso1(){
        withTransaction(()-> {
            Formulario formularioSuscripcionCaso1= new Formulario();
            formularioSuscripcionCaso1.setNombreForm("Formulario Suscripcion Caso 1");

            Pregunta preguntaN = new Pregunta("viandasParaQueEsteLLena", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaVianda = new Pregunta("viandasALLevar", true, TipoPregunta.DESARROLLO);
            OpcionRespuesta whatsapp = new OpcionRespuesta("Whatsapp");
            OpcionRespuesta telefono = new OpcionRespuesta("Telefono");
            OpcionRespuesta telegram = new OpcionRespuesta("Telegram");
            OpcionRespuesta mail = new OpcionRespuesta("Mail");
            List<OpcionRespuesta> tipoContacto = new ArrayList<>();
            tipoContacto.add(whatsapp);
            tipoContacto.add(telegram);
            tipoContacto.add(telefono);
            tipoContacto.add(mail);
            Pregunta preguntaTipoContacto = new Pregunta("tipo_contacto", true, TipoPregunta.OPCION_UNICA, tipoContacto);

            List<Pregunta> preguntas = new ArrayList<>();
            preguntas.add(preguntaN);
            preguntas.add(preguntaVianda);
            preguntas.add(preguntaTipoContacto);

            formularioSuscripcionCaso1.setPreguntas(preguntas);
            formularioRepository.actualizar2(formularioSuscripcionCaso1);

        });
    }
    public void guardarFormularioSuscripcionCaso2(){
        withTransaction(()-> {
            Formulario formularioSuscripcionCaso2= new Formulario();
            formularioSuscripcionCaso2.setNombreForm("Formulario Suscripcion Caso 2");

            Pregunta preguntaHeladera = new Pregunta("Nombre heladera", true, TipoPregunta.DESARROLLO);
            Pregunta preguntaCantidadViandas = new Pregunta("Cantidad Viandas para que este llena", true, TipoPregunta.DESARROLLO);

            Pregunta preguntaCantidadViandas2 = new Pregunta("Cantidad de viandas para llevar a heladera menos llena", true, TipoPregunta.DESARROLLO);
            OpcionRespuesta whatsapp = new OpcionRespuesta("Whatsapp");
            OpcionRespuesta telefono = new OpcionRespuesta("Telefono");
            OpcionRespuesta telegram = new OpcionRespuesta("Telegram");
            OpcionRespuesta mail = new OpcionRespuesta("Mail");
            List<OpcionRespuesta> tipoContacto = new ArrayList<>();
            tipoContacto.add(whatsapp);
            tipoContacto.add(telegram);
            tipoContacto.add(telefono);
            tipoContacto.add(mail);
            Pregunta preguntaTipoContacto = new Pregunta("tipo_contacto", true, TipoPregunta.OPCION_MULTIPLE, tipoContacto);
            Pregunta preguntaContacto = new Pregunta("Contacto", true, TipoPregunta.DESARROLLO);


            List<Pregunta> preguntas = new ArrayList<>();
            preguntas.add(preguntaHeladera);
            preguntas.add(preguntaCantidadViandas);
            preguntas.add(preguntaCantidadViandas2);
            preguntas.add(preguntaTipoContacto);
            preguntas.add(preguntaContacto);
            formularioSuscripcionCaso2.setPreguntas(preguntas);
            formularioRepository.actualizar2(formularioSuscripcionCaso2);

        });
    }
    public void guardarFormularioSuscripcionCaso3(){
        withTransaction(()-> {
            Formulario formularioSuscripcionCaso3= new Formulario();
            formularioSuscripcionCaso3.setNombreForm("Formulario Suscripcion Caso 3");
            Pregunta preguntaHeladera = new Pregunta("Nombre heladera", true, TipoPregunta.DESARROLLO);

            OpcionRespuesta whatsapp = new OpcionRespuesta("Whatsapp");
            OpcionRespuesta telefono = new OpcionRespuesta("Telefono");
            OpcionRespuesta telegram = new OpcionRespuesta("Telegram");
            OpcionRespuesta mail = new OpcionRespuesta("Mail");
            List<OpcionRespuesta> tipoContacto = new ArrayList<>();
            tipoContacto.add(whatsapp);
            tipoContacto.add(telegram);
            tipoContacto.add(telefono);
            tipoContacto.add(mail);
            Pregunta preguntaTipoContacto = new Pregunta("tipo_contacto", true, TipoPregunta.OPCION_MULTIPLE, tipoContacto);
            Pregunta preguntaContacto = new Pregunta("Contacto", true, TipoPregunta.DESARROLLO);

            List<Pregunta> preguntas = new ArrayList<>();
            preguntas.add(preguntaTipoContacto);
            preguntas.add(preguntaContacto);
            preguntas.add(preguntaHeladera);
            formularioSuscripcionCaso3.setPreguntas(preguntas);
            formularioRepository.actualizar2(formularioSuscripcionCaso3);

        });
    }


    private void guardarFormularioRespondido(){
        withTransaction(()->{
            List<OpcionRespuesta> respuesta = new ArrayList<>();
            respuesta.add(opcionRespuesta1);

            Respuesta respuesta1 = new Respuesta(pregunta1,respuesta);

            List<Respuesta> respuestas = new ArrayList<>();
            respuestas.add(respuesta1);

            FormularioRespondido formularioRespondido = new FormularioRespondido(formulario,respuestas, LocalDateTime.now());
            formularioRespondidoRepository.actualizar2(formularioRespondido);
        });
    }

    private  void guardarCanjes(){
        withTransaction(()->{
            Oferta oferta = new Oferta("Cartuchera");

            Rubro Libreria = new Rubro("Libreria");
            Producto producto1 = new Producto("Lapiceras");
            oferta.setRubro(Libreria);
            oferta.setPuntosNecesarios(250);
            oferta.setImagen("productos1.jpg");

            List<Producto> productos = new ArrayList<>();
            productos.add(producto1);
            oferta.setProductos(productos);


            Oferta oferta2 = new Oferta("Libreta");
            oferta2.setRubro(Libreria);
            Producto producto2 = new Producto("Libreta");
            oferta2.setPuntosNecesarios(300);
            oferta2.setImagen("productos2.jpg");

            List<Producto> productos2 = new ArrayList<>();
            productos2.add(producto2);
            oferta2.setProductos(productos);
            ofertaRepository.actualizar2(oferta);
            ofertaRepository.actualizar2(oferta2);

        });

    }

}