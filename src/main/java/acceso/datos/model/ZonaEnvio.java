package acceso.datos.model;

public class ZonaEnvio {
    private int idZona;
    private String nombreZona;
    private double tarifaEnvio;

    // Constructor
    public ZonaEnvio(int idZona, String nombreZona, double tarifaEnvio) {
        this.idZona = idZona;
        this.nombreZona = nombreZona;
        this.tarifaEnvio = tarifaEnvio;
    }

    // Constructor vacío
    public ZonaEnvio() {}

    // Getters y setters
    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public double getTarifaEnvio() {
        return tarifaEnvio;
    }

    public void setTarifaEnvio(double tarifaEnvio) {
        this.tarifaEnvio = tarifaEnvio;
    }

    @Override
    public String toString() {
        return "ZonaEnvio{" +
                "idZona=" + idZona +
                ", nombreZona='" + nombreZona + '\'' +
                ", tarifaEnvio=" + tarifaEnvio +
                '}';
    }
}