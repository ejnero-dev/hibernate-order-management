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
    public Pedido() {
    }

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

    public double getImporteTotal() {
        return importeTotal;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        if (fecha.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha no puede ser futura");
        }
        this.fecha = fecha;
    }

    public void setImporteTotal(double importeTotal) {
        if (importeTotal < 0) {
            throw new IllegalArgumentException("El importe no puede ser negativo");
        }
        if (importeTotal > 999999.99) {
            throw new IllegalArgumentException("El importe excede el máximo permitido");
        }
        this.importeTotal = importeTotal;
    }

    public void setIdCliente(int idCliente) {
        if (idCliente <= 0) {
            throw new IllegalArgumentException("El ID de cliente debe ser positivo");
        }
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