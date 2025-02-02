package acceso.datos.config;

/**
 * Clase que representa las propiedades de conexión a la base de datos.
 */
public class DatabaseProperties {
    private final String url; // URL de la base de datos
    private final String username; // Nombre de usuario para la conexión
    private final String password; // Contraseña para la conexión
    private final int maxPoolSize; // Tamaño máximo del pool de conexiones
    private final int minPoolSize; // Tamaño mínimo del pool de conexiones

    /**
     * Constructor privado que utiliza el patrón Builder.
     *
     * @param builder El objeto Builder que contiene las propiedades.
     */
    private DatabaseProperties(Builder builder) {
        this.url = builder.url;
        this.username = builder.username;
        this.password = builder.password;
        this.maxPoolSize = builder.maxPoolSize;
        this.minPoolSize = builder.minPoolSize;
    }

    /**
     * Devuelve la URL de la base de datos.
     *
     * @return La URL de la base de datos.
     */
    public String getUrl() { return url; }

    /**
     * Devuelve el nombre de usuario para la conexión a la base de datos.
     *
     * @return El nombre de usuario.
     */
    public String getUsername() { return username; }

    /**
     * Devuelve la contraseña para la conexión a la base de datos.
     *
     * @return La contraseña.
     */
    public String getPassword() { return password; }

    /**
     * Devuelve el tamaño máximo del pool de conexiones.
     *
     * @return El tamaño máximo del pool.
     */
    public int getMaxPoolSize() { return maxPoolSize; }

    /**
     * Devuelve el tamaño mínimo del pool de conexiones.
     *
     * @return El tamaño mínimo del pool.
     */
    public int getMinPoolSize() { return minPoolSize; }

    /**
     * Clase interna que implementa el patrón Builder para crear instancias de DatabaseProperties.
     */
    public static class Builder {
        private String url; // URL de la base de datos
        private String username = ""; // Nombre de usuario (opcional)
        private String password = ""; // Contraseña (opcional)
        private int maxPoolSize = 10; // Tamaño máximo del pool (opcional)
        private int minPoolSize = 1; // Tamaño mínimo del pool (opcional)

        /**
         * Establece la URL de la base de datos.
         *
         * @param url La URL de la base de datos.
         * @return El objeto Builder para encadenamiento.
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * Establece el nombre de usuario para la conexión a la base de datos.
         *
         * @param username El nombre de usuario.
         * @return El objeto Builder para encadenamiento.
         */
        public Builder username(String username) {
            this.username = username;
            return this;
        }

        /**
         * Establece la contraseña para la conexión a la base de datos.
         *
         * @param password La contraseña.
         * @return El objeto Builder para encadenamiento.
         */
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * Establece el tamaño máximo del pool de conexiones.
         *
         * @param maxPoolSize El tamaño máximo del pool.
         * @return El objeto Builder para encadenamiento.
         */
        public Builder maxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return this;
        }

        /**
         * Establece el tamaño mínimo del pool de conexiones.
         *
         * @param minPoolSize El tamaño mínimo del pool.
         * @return El objeto Builder para encadenamiento.
         */
        public Builder minPoolSize(int minPoolSize) {
            this.minPoolSize = minPoolSize;
            return this;
        }

        /**
         * Construye una instancia de DatabaseProperties con las propiedades configuradas.
         *
         * @return Una nueva instancia de DatabaseProperties.
         */
        public DatabaseProperties build() {
            if (url == null || url.isEmpty()) {
                throw new IllegalStateException("URL es requerida");
            }
            return new DatabaseProperties(this);
        }
    }
}