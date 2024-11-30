package Domain.models.entities.tarjeta;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.personaVulnerable.PersonaVulnerable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

    @Getter
    @Setter
    @Entity
    @Table(name ="tarjetadeheladera")

    public class TarjetaDeHeladeras{

        @Id
        @GeneratedValue
        @Column(name ="id")
        private Long Id;

        @Column(name ="cantidadMaxUsosPorDia")
        private Integer cantidadMaxUsosPorDia;

        @Column(name ="cantidadUsosPorDia")
        private Integer cantidadUsosPorDia = 0;

        @Column(name = "ultimaVezReiniciado", columnDefinition = "DATE")
        private LocalDate ultimaVezReiniciado;

        @OneToMany
        @JoinColumn (name= "tarjetaHeladeras_id")
        private Set<UsoDeTarjeta> usosDeTarjeta;

        @OneToOne
        @JoinColumn(name = "titularPersonaVulnerable_id")
        private PersonaVulnerable titularTarjeta;

        @Column(name ="entregada")
        private Boolean entregada = false;

        // fijarme si se puede usar la tarjeta (cantidadUsosPorDia < cantidadMaxUsos) sino tirar error?
        // agregar un usoDeTarjeta (add new...)
        // cantidadUsosPorDia ++;

        // el contador de usos por día se tendría que reiniciar cada día (fijarse la ultima fecha que lo uso y si es distinta reiniciarlo)
        //                                                                 no puedo hacer eso porque los set no tienen orden entonces no puedo agarrar el último

        public TarjetaDeHeladeras() {
            this.cantidadUsosPorDia = 0;
            //this.ultimaVezReiniciado = LocalDate.now();
            this.usosDeTarjeta = new HashSet<>();
        }

        public void utilizarTarjeta(Heladera unaHeladera){
            this.calcularCantMaxUsosPorDia();
            this.reiniciarCantidadDeUsosPorDia();
            if(this.cantidadUsosPorDia >= this.cantidadMaxUsosPorDia){
                throw new RuntimeException("Ya alcanzo el uso máximo de usos por el día de hoy");
            }else {
                this.usosDeTarjeta.add(new UsoDeTarjeta (unaHeladera));
                this.cantidadUsosPorDia ++;
            }
        }

        public void reiniciarCantidadDeUsosPorDia() {
            if(!this.ultimaVezReiniciado.equals(LocalDate.now())){  // si la fecha de la ultima vez que se reinicio es distinta a la fecha de hoy, se reinicia
                setCantidadUsosPorDia(0);
                this.setUltimaVezReiniciado(LocalDate.now());
            }
        }

        public void calcularCantMaxUsosPorDia(){  //  4 + 2 por cada menor
            Integer cantidad = 4 + (this.titularTarjeta.cantidadMenoresACargo() * 2);
            setCantidadMaxUsosPorDia(cantidad);
        }

        public Boolean estaDisponible(){
            return !entregada;
        }

        public void meEntregaron( PersonaVulnerable personaVulnerable){
            this.entregada = true;
            this.titularTarjeta = personaVulnerable;
        }
    }

