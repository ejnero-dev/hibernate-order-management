package acceso.datos.model;

import java.time.LocalDate;

/**
 * Clase que representa un pedido en la base de datos.
 */
public class Pedido {
    private int idPedido; // Identificador único del pedido
    private LocalDate fecha; // Fecha en que se realizó el pedido
    private double importeTotal; // Importe total del pedido
    private int idCliente; // ID del cliente que realizó el pedido

    /**
     * Constructor completo para la clase Pedido.
     *
     * @param idPedido      Identificador único del pedido
     * @param fecha         Fecha en que se realizó el pedido
     * @param importeTotal  Importe total del pedido
     * @param idCliente     ID del cliente que realizó el pedido
     */
    public Pedido(int idPedido, LocalDate fecha, double importeTotal, int idCliente) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.importeTotal = importeTotal;
        this.idCliente = idCliente;
    }

    /**
     * Constructor vacío para la clase Pedido.
     */
    public Pedido() {
    }

    /**
     * Obtiene el identificador del pedido.
     *
     * @return Identificador del pedido
     */
    public int getIdPedido() {
        return idPedido;
    }

    /**
     * Establece el identificador del pedido.
     *
     * @param idPedido Nuevo identificador del pedido
     */
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    /**
     * Obtiene la fecha en que se realizó el pedido.
     *
     * @return Fecha del pedido
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha en que se realizó el pedido.
     *
     * @param fecha Nueva fecha del pedido
     * @throws IllegalArgumentException Si la fecha es nula o futura
     */
    public void setFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        if (fecha.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha no puede ser futura");
        }
        this.fecha = fecha;
    }

    /**
     * Obtiene el importe total del pedido.
     *
     * @return Importe total del pedido
     */
    public double getImporteTotal() {
        return importeTotal;
    }

    /**
     * Establece el importe total del pedido.
     *
     * @param importeTotal Nuevo importe total del pedido
     * @throws IllegalArgumentException Si el importe es negativo o excede el máximo permitido
     */
    public void setImporteTotal(double importeTotal) {
        if (importeTotal < 0) {
            throw new IllegalArgumentException("El importe no puede ser negativo");
        }
        if (importeTotal > 999999.99) {
            throw new IllegalArgumentException("El importe excede el máximo permitido");
        }
        this.importeTotal = importeTotal;
    }

    /**
     * Obtiene el ID del cliente que realizó el pedido.
     *
     * @return ID del cliente
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * Establece el ID del cliente que realizó el pedido.
     *
     * @param idCliente Nuevo ID del cliente
     * @throws IllegalArgumentException Si el ID es no positivo
     */
    public void setIdCliente(int idCliente) {
        if (idCliente <= 0) {
            throw new IllegalArgumentException("El ID de cliente debe ser positivo");
        }
        this.idCliente = idCliente;
    }

    /**
     * Devuelve una representación en cadena del objeto Pedido.
     *
     * @return Cadena que representa al objeto Pedido
     */
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