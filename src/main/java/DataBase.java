
import java.sql.*;

public class DataBase {
    private String dbURL = "jdbc:oracle:thin:@localhost:1521:XE";
    private String userID = "loko_lab";
    private String password = "pass";
    private Connection connection;

    private static DataBase dataBase = new DataBase();

    public static DataBase getInstance() {
        return dataBase;
    }

    private DataBase() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            connection = DriverManager.getConnection(dbURL, userID, password);
        }
        catch (Exception e) {
            connection = null;
        }
    }

    public ResultSet executeTest() {
        try {
            Statement sqlStatement = connection.createStatement();
            String readRecordSQL = "select * from CUSTOMER";
            return sqlStatement.executeQuery(readRecordSQL);
        }
        catch (Exception e) {
            return null;
        }
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    public ResultSet executeRequest(String request) {
        try {
            Statement sqlStatement = connection.createStatement();
            return sqlStatement.executeQuery(request);
        }
        catch (Exception e) {
            return null;
        }
    }
}