package cs601.project4;

public class Config {
    private String database;
    private String username;
    private String password;

    public Config(String database, String username, String password) {
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
