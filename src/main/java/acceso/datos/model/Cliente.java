package acceso.datos.model;

public class Cliente {
    private int idCliente;
    private String nombre;
    private String email;
    private String telefono;
    private int idZona;

    // Constructor completo
    public Cliente(int idCliente, String nombre, String email, String telefono, int idZona) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.idZona = idZona;
    }

    // Constructor vacío
    public Cliente() {
    }

    // Getters y setters
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getIdZona() {
        return idZona;
    }

    @Override
    public String toString() {
        return nombre + " (" + email + ")";
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }
        this.nombre = nombre.trim();
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
        if (email.length() > 100) {
            throw new IllegalArgumentException("El email no puede exceder 100 caracteres");
        }
        this.email = email.trim();
    }

    public void setTelefono(String telefono) {
        if (telefono != null) {
            telefono = telefono.trim();
            if (!telefono.matches("^[0-9]{9}$")) {
                throw new IllegalArgumentException("El teléfono debe tener 9 dígitos");
            }
        }
        this.telefono = telefono;
    }

    public void setIdZona(int idZona) {
        if (idZona <= 0) {
            throw new IllegalArgumentException("El ID de zona debe ser positivo");
        }
        this.idZona = idZona;
    }
}