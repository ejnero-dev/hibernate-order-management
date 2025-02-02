package acceso.datos.model;

import java.time.LocalDate;

public class Pedido {
    private int idPedido;
    private LocalDate fecha;
    private double importeTotal;
    private int idCliente;

    // Constructor completo
    public Pedido(int idPedido, LocalDate fecha, double importeTotal, int idCliente) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.importeTotal = importeTotal;
        this.idCliente = idCliente;
    }

    // Constructor vacío
    public Pedido() {}

    // Getters y setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", fecha=" + fecha +
                ", importeTotal=" + importeTotal +
                ", idCliente=" + idCliente +
                '}';
    }
}