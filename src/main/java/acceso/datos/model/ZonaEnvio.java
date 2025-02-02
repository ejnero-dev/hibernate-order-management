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
    public ZonaEnvio() {
    }

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

    public double getTarifaEnvio() {
        return tarifaEnvio;
    }

    public void setNombreZona(String nombreZona) {
        if (nombreZona == null || nombreZona.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de zona no puede estar vacío");
        }
        if (nombreZona.length() > 100) {
            throw new IllegalArgumentException("El nombre de zona no puede exceder 100 caracteres");
        }
        this.nombreZona = nombreZona.trim();
    }

    public void setTarifaEnvio(double tarifaEnvio) {
        if (tarifaEnvio < 0) {
            throw new IllegalArgumentException("La tarifa no puede ser negativa");
        }
        if (tarifaEnvio > 999.99) {
            throw new IllegalArgumentException("La tarifa excede el máximo permitido");
        }
        this.tarifaEnvio = tarifaEnvio;
    }

    @Override
    public String toString() {
        return String.format("%s (Tarifa: %.2f€)", nombreZona, tarifaEnvio);
    }
}