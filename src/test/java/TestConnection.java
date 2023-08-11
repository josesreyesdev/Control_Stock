import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        String url = "";
        String user = "sa";
        String password = "root1234";

        connection(url, user, password);
    }

    private static void connection(String url, String user, String password) {
        try {
            Connection connectionSql = DriverManager.getConnection(url, user, password);
            System.out.println("Cerrando la conexion");
            connectionSql.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
