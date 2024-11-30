package Domain.controller.ColaboracionController;

import Domain.DTO.TecnicoDTO;
import Domain.models.entities.Ubicacion.*;
import Domain.models.entities.Usuario.TipoPermiso;
import Domain.models.entities.Usuario.Rol;
import Domain.models.entities.Usuario.TipoRol;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.*;
import Domain.models.entities.formasDeColaborar.*;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.Modelo;
import Domain.models.entities.personaVulnerable.PersonaVulnerable;
import Domain.models.entities.personaVulnerable.TipoDocumento;
import Domain.models.entities.tarjeta.TarjetaDeHeladeras;
import Domain.models.entities.tecnico.Tecnico;
import Domain.models.entities.vianda.Comida;
import Domain.models.entities.vianda.Vianda;
import Domain.models.repository.*;
import Domain.utils.ICrudViewsHandler;
import com.google.gson.Gson;
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
import java.util.stream.Collectors;

public class FormaColaboracionHController implements ICrudViewsHandler, WithSimplePersistenceUnit {
    FormaColaboracionRepository formaColaboracionRepository;
    HeladeraRepository heladeraRepository;
    UsuarioRepository usuarioRepository;
    PersonaVulnerableRepository personaVulnerableRepository;
    OfertaRepository ofertaRepository;
    ColaboradorRepository colaboradorRepository;
    ViandaRepository viandaRepository;
    TarjetaHeladerasRepository tarjetaHeladerasRepository;
    TarjetaDistribucionViandaRepository tarjetaDistribucionViandaRepository;
    ModeloRepository modeloRepository;
    TecnicoRepository tecnicoRepository;


    public FormaColaboracionHController(FormaColaboracionRepository formaColaboracionRepository, HeladeraRepository heladeraRepository, UsuarioRepository usuarioRepository, PersonaVulnerableRepository personaVulnerableRepository, OfertaRepository ofertaRepository, ColaboradorRepository colaboradorRepository, ViandaRepository viandaRepository, TarjetaHeladerasRepository tarjetaHeladerasRepository, TarjetaDistribucionViandaRepository tarjetaDistribucionViandaRepository, ModeloRepository modeloRepository, TecnicoRepository tecnicoRepository) {
        this.formaColaboracionRepository = formaColaboracionRepository;
        this.heladeraRepository = heladeraRepository;
        this.usuarioRepository = usuarioRepository;
        this.personaVulnerableRepository = personaVulnerableRepository;
        this.ofertaRepository = ofertaRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.viandaRepository = viandaRepository;
        this.tarjetaHeladerasRepository = tarjetaHeladerasRepository;
        this.tarjetaDistribucionViandaRepository = tarjetaDistribucionViandaRepository;
        this.modeloRepository = modeloRepository;
        this.tecnicoRepository = tecnicoRepository;

    }

    @Override
    public void index(Context context) {


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
    public void show(Context context) {
        context.render("colaboracionSuccess.hbs");
    }

    @Override
    public void create(Context context) {
        Map<String, Object> model = new HashMap<>();


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

        Rol rol = usuario.getRol();

        // Variables booleanas para la vista
        boolean puedeDonarDinero = ((rol.getTipo().equals(TipoRol.PERSONA_HUMANA) || rol.getTipo().equals(TipoRol.PERSONA_JURIDICA)) && rol.getPermisos().contains(TipoPermiso.DONACIONMONETARIA));
        boolean puedeDistribuirVianda = rol.getTipo().equals(TipoRol.PERSONA_HUMANA) && rol.getPermisos().contains(TipoPermiso.DISTRIBUCIONVIANDAS);
        boolean puedeDonarVianda = rol.getTipo().equals(TipoRol.PERSONA_HUMANA) && rol.getPermisos().contains(TipoPermiso.DONACIONVIANDAS);
        boolean puedeRegistrarPersonas = rol.getTipo().equals(TipoRol.PERSONA_HUMANA) && rol.getPermisos().contains(TipoPermiso.REGISTROPERSONAVULNERABLE);
        boolean puedeEncargarHeladera = rol.getTipo().equals(TipoRol.PERSONA_JURIDICA) && rol.getPermisos().contains(TipoPermiso.ENCARGARSEHELADERA);
        boolean puedeOfrecerProductos = rol.getTipo().equals(TipoRol.PERSONA_JURIDICA) && rol.getPermisos().contains(TipoPermiso.OFRECERPRODUCTOS);

        if(!rol.getTipo().equals(TipoRol.PERSONA_HUMANA)){
            System.out.println("No Es humano");
        }
        if(!rol.getPermisos().contains(TipoPermiso.DONACIONMONETARIA)){
            System.out.println("Puede donar");
        }

        // Pasamos las variables al contexto
        model.put("puedeDonarDinero", puedeDonarDinero);
        model.put("puedeDistribuirVianda", puedeDistribuirVianda);
        model.put("puedeDonarVianda", puedeDonarVianda);
        model.put("puedeRegistrarPersonas", puedeRegistrarPersonas);
        model.put("puedeEncargarHeladera", puedeEncargarHeladera);
        model.put("puedeOfrecerProductos", puedeOfrecerProductos);

        //envio heladeras
        List<Heladera> heladeras = heladeraRepository.buscarTodos();
        model.put("heladeras", heladeras);

        Set<Tecnico> tecnicos = tecnicoRepository.buscarTodos();

        List<TecnicoDTO> tecnicosDTO = tecnicos.stream()
                .map(tecnico -> new TecnicoDTO(tecnico.getNombre(), tecnico.getAreaDeCobertura().getPuntoGeografico().getLatitud(), tecnico.getAreaDeCobertura().getPuntoGeografico().getLongitud(),
                        tecnico.getAreaDeCobertura().getRadioDeCobertura())).collect(Collectors.toList());

        Gson gson = new Gson();

        String tecnicosJson = gson.toJson(tecnicosDTO);
        model.put("tecnicosJson", tecnicosJson);

        List<Modelo> modelos = modeloRepository.buscarTodos();
        model.put("modelos", modelos);  // Pasamos la lista de modelos al template
        context.render("colaboraciones.hbs", model);
    }

    @Override
    public void save(Context context) {
        String action = context.formParam("action");
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
        Rol rol = usuario.getRol();
        Colaborador colaborador = colaboradorRepository.buscarPorUsuario(usuario);

        if (colaborador == null) {
            context.status(404).result("Colaborador no encontrado");
            return;
        }

        switch (action) {
            case "donacionMonetaria": {
                DonacionMonetaria donacion = new DonacionMonetaria();
                String monto = context.formParam("monto_donacion");
                Integer monto_real = Integer.valueOf(monto);
                donacion.setMonto(monto_real);

                Frecuencia frecuencia = new Frecuencia();
                donacion.setFrecuencia(frecuencia);

                Integer repeticion = Integer.valueOf(context.formParam("repeticion"));
                frecuencia.setRepeticion(repeticion);

                String periodo = context.formParam("periodo");
                if (periodo.equalsIgnoreCase("ANUALMEMENTE")) {
                    frecuencia.setPeriodo(Periodo.ANUALMENTE);
                } else if (periodo.equalsIgnoreCase("MENSUALEMENTE")) {
                    frecuencia.setPeriodo(Periodo.MENSUALMENTE);
                } else if (periodo.equalsIgnoreCase("SEMANALMENTE")) {
                    frecuencia.setPeriodo(Periodo.SEMANALMENTE);

                }
                donacion.setFecha(LocalDate.now());

                MedioDePago medioDePago = new MedioDePago();
                String tipoMedioDePago = context.formParam("medioPago");
                if (tipoMedioDePago.equalsIgnoreCase("credito")) {
                    medioDePago.setTipoMedioPago(TipoMedioPago.CREDITO);
                } else if (tipoMedioDePago.equalsIgnoreCase("debito")) {
                    medioDePago.setTipoMedioPago(TipoMedioPago.DEBITO);
                }
                Integer numeroTarjeta = Integer.valueOf(Objects.requireNonNull(context.formParam("numeroTarjeta")));
                medioDePago.setNumeroTarjeta(numeroTarjeta);

                donacion.setMedioDePago(medioDePago);

                Double puntosASumar = donacion.calcularPuntos();
                Double puntos = colaborador.getPuntos();
                colaborador.setPuntos(puntos + puntosASumar);

                try {
                    beginTransaction();
                    formaColaboracionRepository.guardar(donacion);
                    colaborador.getColaboraciones().add(donacion);
                    colaboradorRepository.actualizar(colaborador);
                    commitTransaction();
                    context.redirect("/colaboracionSuccess");
                } catch (Exception e) {
                    e.printStackTrace();
                    context.status(500).result("Ha ocurrido un error inesperado.");
                }

                break;
            }

            case "donacionViandas": {
                DonacionViandas donacionViandas = new DonacionViandas();

                donacionViandas.setFecha(LocalDate.now());

                Long heladeraId = Long.valueOf((context.formParam("heladera")));
                Heladera heladera = this.heladeraRepository.buscarPorID(heladeraId);
                if (heladera == null) {
                    context.status(213).result("Error no existe la heladera: " + heladeraId);
                } else {
                    donacionViandas.setHeladeraUtilizada(heladera);
                }

                // Crear lista de viandas
                Set<Vianda> viandas = new HashSet<>();

                Integer cantViandas = Integer.parseInt(context.formParam("cantidad"));
                donacionViandas.setCantidadViandas(cantViandas);
                String comidaNombre = context.formParam("nombreComida");
                Integer calorias = Integer.parseInt(context.formParam("calorias"));
                Integer peso = Integer.parseInt(context.formParam("peso"));
                LocalDate caducidad = LocalDate.parse((context.formParam("caducidad")));

                Comida comida = new Comida();
                comida.setFechaDeCaducidad((caducidad));
                comida.setNombre(comidaNombre);
                comida.setCalorias(calorias);

                for (int i = 0; i < cantViandas; i++) {
                    Vianda vianda = new Vianda();
                    vianda.setUnaComida(comida);
                    vianda.setPeso(peso);
                    vianda.setUnColaborador(colaborador);
                    viandas.add(vianda);
                    beginTransaction();
                    viandaRepository.guardar(vianda);
                    commitTransaction();
                }

                donacionViandas.setViandasDonadas(viandas);
                colaborador.getColaboraciones().add(donacionViandas);

                Double puntosASumar = donacionViandas.calcularPuntos();
                Double puntos = colaborador.getPuntos();
                colaborador.setPuntos(puntos + puntosASumar);

                try {
                    beginTransaction();
                    formaColaboracionRepository.guardar(donacionViandas);
                    colaboradorRepository.actualizar(colaborador);
                    commitTransaction();
                    context.redirect("/colaboracionSuccess");
                } catch (Exception e) {
                    e.printStackTrace();
                    context.status(500).result("Ha ocurrido un error inesperado.");
                }


                break;
            }

            case "registroPersonaVulnerable": {
                PersonaVulnerable personaVulnerable = new PersonaVulnerable();
                personaVulnerable.setNombrePersona(context.formParam("nombre"));
                personaVulnerable.setApellido(context.formParam("apellido"));

                String tipoDocumento = context.formParam("tipo_documento");
                if (tipoDocumento.equalsIgnoreCase("DNI")) {
                    personaVulnerable.setTipoDocumento(TipoDocumento.DNI);
                } else if (tipoDocumento.equalsIgnoreCase("LIBRETAENROLAMIENTO")) {
                    personaVulnerable.setTipoDocumento(TipoDocumento.LIBRETAENROLAMIENTO);
                } else if (tipoDocumento.equalsIgnoreCase("LIBRETACIVICA")) {
                    personaVulnerable.setTipoDocumento(TipoDocumento.LIBRETACIVICA);
                }

                try {
                    int nroDocumento = Integer.parseInt(context.formParam("documento"));
                    personaVulnerable.setNroDocumento(nroDocumento);
                } catch (NumberFormatException e) {
                    context.status(400).result("Documento inválido");
                    return;
                }
                String fechaNacimientoStr = context.formParam("nacimiento");
                if (fechaNacimientoStr != null && !fechaNacimientoStr.isEmpty()) {
                    personaVulnerable.setFechaDeNacimiento(LocalDate.parse(fechaNacimientoStr));
                } else {
                    context.status(400).result("El campo 'nacimiento' está vacío o es nulo.");
                }

                personaVulnerable.setFechaRegistro(LocalDate.now());

                String altura = context.formParam("altura");
                String calle = context.formParam("calle");
                String localidad = context.formParam("localidad");
                String partido_nombre = context.formParam("partido");
                String nombre_provincia = context.formParam("provincia");

                Direccion unDomicilio = new Direccion();

                if (altura != null && !altura.isEmpty() && calle != null && !calle.isEmpty()
                        && localidad != null && !localidad.isEmpty()
                        && partido_nombre != null && !partido_nombre.isEmpty()
                        && nombre_provincia != null && !nombre_provincia.isEmpty()) {

                    /* Chequear si es necesario ver si ya está creada en la BD**/
                    Provincia provincia1 = new Provincia();
                    provincia1.setNombre(nombre_provincia);

                    Partido partido = new Partido();
                    partido.setNombre(partido_nombre);
                    partido.setProvincia(provincia1);


                    unDomicilio.setCalle(calle);
                    unDomicilio.setAltura(Integer.valueOf(altura));

                    Localidad localidad1 = new Localidad();
                    localidad1.setNombre(localidad);
                    unDomicilio.setLocalidad(localidad1);
                    personaVulnerable.setUnDomicilio(unDomicilio);

                    personaVulnerable.setSituacionDeCalle(false);

                } else {
                    personaVulnerable.setSituacionDeCalle(true);
                    unDomicilio = null;
                }

                Integer cantidad = 1;
                String tieneMenoresStr = context.formParam("menor");
                Boolean tieneMenores = false;
                if (tieneMenoresStr.equals("SI")) {
                    tieneMenores = true;
                }
                personaVulnerable.setTienePersonasACargo(tieneMenores);
                personaVulnerable.setMenoresACargo(new HashSet<>());

                List<String> nombresMenores = context.formParams("nombreMenor[]");
                if (nombresMenores.isEmpty()) {
                    throw new RuntimeException();
                }
                List<String> apellidosMenores = context.formParams("apellidoMenor[]");
                List<String> tipoDocumentoMenores = context.formParams("tipo_documento_menor[]");
                List<String> documentoMenores = context.formParams("documentoMenor[]");
                List<String> nacimientoMenores = context.formParams("nacimientoMenor[]");

                if (tieneMenores) {
                    for (int i = 0; i < nombresMenores.size(); i++) {
                        cantidad++;

                        PersonaVulnerable menor = new PersonaVulnerable();

                        menor.setNombrePersona(nombresMenores.get(i));
                        menor.setApellido(apellidosMenores.get(i));

                        String tipoDocumentoMenor = tipoDocumentoMenores.get(i);
                        if (tipoDocumentoMenor != null) {
                            if (tipoDocumentoMenor.equalsIgnoreCase("DNI")) {
                                menor.setTipoDocumento(TipoDocumento.DNI);
                            } else if (tipoDocumentoMenor.equalsIgnoreCase("LIBRETAENROLAMIENTO")) {
                                menor.setTipoDocumento(TipoDocumento.LIBRETAENROLAMIENTO);
                            } else if (tipoDocumentoMenor.equalsIgnoreCase("LIBRETACIVICA")) {
                                menor.setTipoDocumento(TipoDocumento.LIBRETACIVICA);
                            }
                        } else {
                            context.status(500).result("El tipo de documento del menor es null para el menor en posición " + i);
                        }

                        menor.setFechaDeNacimiento(LocalDate.parse(nacimientoMenores.get(i)));
                        menor.setNroDocumento(Integer.parseInt(documentoMenores.get(i)));
                        menor.setFechaRegistro(LocalDate.now());
                        menor.setTienePersonasACargo(false);
                        menor.setMenoresACargo(new HashSet<>());
                        menor.setUnDomicilio(unDomicilio);
                        menor.setSituacionDeCalle(unDomicilio == null);

                        try {
                            personaVulnerable.agregarMenor(menor);
                            beginTransaction();
                            this.personaVulnerableRepository.guardar(menor);
                            commitTransaction();
                        } catch (Exception e) {
                            context.status(500).result("Ha ocurrido un error inesperado." + e.getMessage());
                        }
                    }

                }
                RegistroPersonaVulnerable registro = new RegistroPersonaVulnerable(colaborador, personaVulnerable);
                registro.setCantidad(cantidad);

                TarjetaDeHeladeras tarjetaPersona = colaborador.entregarTarjeta(personaVulnerable);
                tarjetaPersona.calcularCantMaxUsosPorDia();

                personaVulnerable.setTarjetaDeHeladera(tarjetaPersona);
                colaborador.sumarTarjetaEntregada();

                Double puntosASumar = registro.calcularPuntos();
                colaborador.sumarPuntos(puntosASumar);

                try {
                    beginTransaction();
                    this.personaVulnerableRepository.guardar(personaVulnerable);
                    this.formaColaboracionRepository.guardar(registro);
                    this.colaboradorRepository.actualizar(colaborador);
                    this.tarjetaHeladerasRepository.actualizar(tarjetaPersona);
                    commitTransaction();
                    context.redirect("/colaboracionSuccess");

                } catch (Exception e) {
                    context.status(500).result("Ha ocurrido un error inesperado." + e.getMessage());
                }
                break;
            }

            case "distribucionVianda": {
                DistribucionViandas distribucionViandas = new DistribucionViandas();

                Long heladeraId = Long.valueOf((context.formParam("heladeraOrigen")));
                Heladera heladera = heladeraRepository.buscarPorID(heladeraId);
                if (heladera == null) {
                    context.status(500).result("Error no existe la heladera ");
                } else {
                    distribucionViandas.setHeladeraOrigen(heladera);
                }

                List<String> heladerasStrings = context.formParams("heladera[]");
                if(heladerasStrings == null){
                    context.status(500).result("Error no se cargaron las heladeras ");
                }
                List<Long> heladerasDestino = new ArrayList<>();

                for (String heladerasString : heladerasStrings) {
                    heladerasDestino.add(Long.valueOf(heladerasString));
                }

                for (Long aLong : heladerasDestino) {
                    Heladera heladeraDestino = heladeraRepository.buscarPorID(aLong);
                    if (heladeraDestino == null) {
                        context.status(500).result("Error no existe la heladera ");
                    } else {
                        distribucionViandas.agregarHeladeraDestino(heladeraDestino);
                    }
                }

                List<String> cantidadedStr = context.formParams("cantidad[]");

                for (String cantidad : cantidadedStr) {
                    Integer cant = Integer.valueOf(cantidad);
                    distribucionViandas.sumarViandas(cant);
                }

                distribucionViandas.setTarjeta(colaborador.getTarjetaDistribucionViandas());
                distribucionViandas.setFecha(LocalDate.now());

                String motivoDistribucion = context.formParam("motivo");
                if (motivoDistribucion != null) {
                    if (motivoDistribucion.equalsIgnoreCase("SOBRAN_VIANDAS")) {
                        distribucionViandas.setMotivoDistribucion(MotivoDistribucion.SOBRAN_VIANDAS);
                    } else if (motivoDistribucion.equalsIgnoreCase("DESPERFECTO_HELADERA")) {
                        distribucionViandas.setMotivoDistribucion(MotivoDistribucion.DESPERFECTO_HELADERA);
                    } else if (motivoDistribucion.equalsIgnoreCase("FALTA_VIANDAS")) {
                        distribucionViandas.setMotivoDistribucion(MotivoDistribucion.FALTA_VIANDAS);
                    }
                } else {
                    context.status(500).result("El motivo de distribucion no es correcto" );
                }

                Double puntosASumar = distribucionViandas.calcularPuntos();
                Double puntos = colaborador.getPuntos();
                colaborador.setPuntos(puntos + puntosASumar);

                try {
                    beginTransaction();
                    this.formaColaboracionRepository.guardar(distribucionViandas);
                    this.colaboradorRepository.actualizar(colaborador);
                    commitTransaction();
                    context.redirect("/colaboracionSuccess");
                } catch (Exception e) {
                    e.printStackTrace();
                    context.status(500).result("Ha ocurrido un error inesperado.");
                }
                break;

            }case "encargoHeladera": {
                EncargarseHeladera encargarseHeladera = new EncargarseHeladera();
                encargarseHeladera.setFecha(LocalDate.now());

                encargarseHeladera.setColaboradorEncargado(colaborador);

                String nombreHeladera = (context.formParam("heladera"));

                Heladera heladera = this.heladeraRepository.buscarPorNombre(nombreHeladera);
                if (heladera != null) {
                    context.status(213).result("Error ya existe una heladera con ese nombre: " + nombreHeladera);
                } else {
                    Heladera otraHeladera = new Heladera(nombreHeladera);

                    String altura = context.formParam("altura");
                    String calle = context.formParam("calle");
                    String localidad = context.formParam("localidad");
                    String partido_nombre = context.formParam("partido");
                    String nombre_provincia = context.formParam("provincia");

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
                        otraHeladera.setEstaActiva(true);

                        PuntoGeografico puntoGeografico = Geodecodificacion.obtenerCoordenadas(calle, altura, localidad, nombre_provincia);
                        puntoGeografico.setDireccion(unDomicilio);
                        otraHeladera.setUnPuntoGeografico(puntoGeografico);
                        Set<Tecnico> tecnicosExistentes = tecnicoRepository.buscarTodos();

                        if(tecnicosExistentes.stream()
                                .anyMatch(tecnico -> Geodecodificacion.estaDentroDelRadio(
                                        tecnico.getAreaDeCobertura().getPuntoGeografico(),
                                        otraHeladera.getUnPuntoGeografico(),
                                        tecnico.getAreaDeCobertura().getRadioDeCobertura())))
                        {
                            String unModelo = context.formParam("modelo");
                            Modelo modeloEnRepositorio = modeloRepository.buscarPorNombre(unModelo);
                            otraHeladera.setUnModelo(modeloEnRepositorio);

                            Integer capacidad = Integer.valueOf(context.formParam("capacidad"));
                            otraHeladera.setCapacidad(capacidad);
                            otraHeladera.setFechaActivacion(LocalDate.now());
                            colaborador.getHeladerasActivas().add(otraHeladera);
                            encargarseHeladera.setHeladeraACargo(otraHeladera);

                            // Calcular y sumar puntos
                            Double puntosASumar = encargarseHeladera.calcularPuntos();
                            colaborador.sumarPuntos(puntosASumar);

                            try {
                                beginTransaction();
                                this.heladeraRepository.guardar(otraHeladera);
                                this.formaColaboracionRepository.guardar(encargarseHeladera);
                                this.colaboradorRepository.actualizar(colaborador);
                                commitTransaction();
                                context.redirect("/colaboracionSuccess");
                            } catch (Exception e) {
                                e.printStackTrace();
                                context.status(500).result("Ha ocurrido un error inesperado.");
                            }}
                        else {
                            Map<String, Object> model = new HashMap<>();
                            model.put("solicitudRechazada", true);
                            model.put("sinTecnico", "La dirección ingresada no está cubierta por ningun técnico, reingresar dirección.");

                            boolean puedeDonarDinero = ((rol.getTipo().equals(TipoRol.PERSONA_HUMANA) || rol.getTipo().equals(TipoRol.PERSONA_JURIDICA)) && rol.getPermisos().contains(TipoPermiso.DONACIONMONETARIA));
                            boolean puedeEncargarHeladera = rol.getTipo().equals(TipoRol.PERSONA_JURIDICA) && rol.getPermisos().contains(TipoPermiso.ENCARGARSEHELADERA);
                            boolean puedeOfrecerProductos = rol.getTipo().equals(TipoRol.PERSONA_JURIDICA) && rol.getPermisos().contains(TipoPermiso.OFRECERPRODUCTOS);

                            model.put("puedeDonarDinero", puedeDonarDinero);
                            model.put("puedeEncargarHeladera", puedeEncargarHeladera);
                            model.put("puedeOfrecerProductos", puedeOfrecerProductos);

                            context.render("colaboraciones.hbs", model); }
                    }
                }
            }
            break;


            case "ofrecerProductos": {
                OfrecerProductos ofrecerProductos = new OfrecerProductos();
                ofrecerProductos.setFechaOfertas(LocalDate.now());
                Oferta oferta = new Oferta();

                String nombreOferta = context.formParam("nombreOferta");
                oferta.setNombre(nombreOferta);

                String puntosNecesarios = context.formParam("puntosNecesarios");
                oferta.setPuntosNecesarios(Integer.valueOf(puntosNecesarios));

                try {
                    Part foto = context.req().getPart("imagen");
                    if (foto != null && foto.getSize() > 0) {
                        String uploadDir = "src/main/resources/public/assets.img/uploads";
                        File uploadFolder = new File(uploadDir);
                        if (!uploadFolder.exists()) {
                            uploadFolder.mkdirs();
                        }
                        String fileName = Paths.get(foto.getSubmittedFileName()).getFileName().toString();
                        Path filePath = Paths.get(uploadDir, fileName);
                        Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                        oferta.setImagen("assets.img/uploads/" + fileName);  // Guarda la ruta relativa
                    }
                } catch (ServletException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                String rubroOferta = context.formParam("tipoRubro");
                Rubro rubro = new Rubro();
                rubro.setNombreRubro(rubroOferta);
                oferta.setRubro(rubro);

                List<String> productos = context.formParams("nombreProducto[]");

                if (productos != null) {

                    int maxProductos = Math.min(productos.size(), 5);
                    for (int i = 0; i < maxProductos; i++) {
                        String producto = productos.get(i);

                        if (producto != null && !producto.isEmpty()) {
                            Producto producto1 = new Producto();
                            producto1.setNombreProducto(producto);
                            oferta.getProductos().add(producto1);
                        }
                    }
                }

                ofrecerProductos.setOfertasDisponibles(oferta);
                colaborador.getColaboraciones().add(ofrecerProductos);

                colaborador.getOfertasEmpresa().add(oferta);

                Double puntosASumar = ofrecerProductos.calcularPuntos();
                Double puntos = colaborador.getPuntos();
                colaborador.setPuntos(puntos + puntosASumar);

                try {
                    beginTransaction();
                    this.formaColaboracionRepository.guardar(ofrecerProductos);
                    this.ofertaRepository.guardar(oferta);
                    this.colaboradorRepository.actualizar(colaborador);
                    commitTransaction();
                    context.redirect("/colaboracionSuccess");

                } catch (Exception e) {
                    e.printStackTrace();
                    context.status(500).result("Ha ocurrido un error inesperado.");
                }
                break;

            }
        }
    }
}


