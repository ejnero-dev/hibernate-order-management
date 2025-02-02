package acceso.datos.config;

public class DatabaseProperties {
    private final String url;
    private final String username;
    private final String password;
    private final int maxPoolSize;
    private final int minPoolSize;

    private DatabaseProperties(Builder builder) {
        this.url = builder.url;
        this.username = builder.username;
        this.password = builder.password;
        this.maxPoolSize = builder.maxPoolSize;
        this.minPoolSize = builder.minPoolSize;
    }

    // Getters
    public String getUrl() { return url; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getMaxPoolSize() { return maxPoolSize; }
    public int getMinPoolSize() { return minPoolSize; }

    // Builder pattern
    public static class Builder {
        private String url;
        private String username = "";
        private String password = "";
        private int maxPoolSize = 10;
        private int minPoolSize = 1;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder maxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return this;
        }

        public Builder minPoolSize(int minPoolSize) {
            this.minPoolSize = minPoolSize;
            return this;
        }

        public DatabaseProperties build() {
            if (url == null || url.isEmpty()) {
                throw new IllegalStateException("URL is required");
            }
            return new DatabaseProperties(this);
        }
    }
}