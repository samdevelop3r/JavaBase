package javamysql.data_classes;

public record Url(String hostname, Integer port, String username,
                  String password, String databaseName) {

    public String asJDBC() {
        return "jdbc:mysql://".concat(this.hostname).concat(":").concat(String.valueOf(this.port)).concat("/").concat(this.databaseName);
    }
}
