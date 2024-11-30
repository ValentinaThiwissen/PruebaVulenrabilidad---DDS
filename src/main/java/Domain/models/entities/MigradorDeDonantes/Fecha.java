package Domain.models.entities.MigradorDeDonantes;

public class Fecha {
    private Integer dia;
    private Integer mes;
    private Integer anio;

    public Fecha(String s) {
        String[] partes = s.split("/");

        if (partes.length == 3) {
            try {
                dia = Integer.parseInt(partes[0]);
                mes = Integer.parseInt(partes[1]);
                anio = Integer.parseInt(partes[2]);
            } catch (NumberFormatException e) {
                System.out.println("Formato de fecha incorrecto: " + s);
            }
        } else {
            System.out.println("Formato de fecha incorrecto: " + s);
        }
    }
    public void mostrarse(){
        System.out.println("\n" +dia + "/" + mes + "/" + anio);
    }
    public Integer getDia() {
        return dia;
    }
    public Integer getMes() {
        return mes;
    }
    public Integer getAnio() {
        return anio;
    }
}
