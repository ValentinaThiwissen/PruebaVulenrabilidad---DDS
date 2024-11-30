package Domain.services.recomendacionPuntosApi.entities;



public class DatosGeograficos {  //Clase molde

    public Punto punto;
    public static class Punto {
        public Double getLatitud() {
            return latitud;
        }

        public void setLatitud(Double latitud) {
            this.latitud = latitud;
        }

        public Double getLongitud() {
            return longitud;
        }

        public void setLongitud(Double longitud) {
            this.longitud = longitud;
        }

        public Punto(Double latitud, Double longitud) {
            this.latitud = latitud;
            this.longitud = longitud;
        }

        public Double latitud;
        public Double longitud;
    }

    public DatosGeograficos(Punto punto) {
        this.punto = punto;
    }

    public Punto getPunto() {
        return punto;
    }

    public void setPunto(Punto punto) {
        this.punto = punto;
    }

}
