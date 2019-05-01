import java.sql.*;
//import oracle.jdbc.driver.*;

public class Main {
    public static void main(String[] args) {

        /*try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }*/


        try {
            //DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
            Class.forName("oracle.jdbc.OracleDriver");

            String dbURL = "jdbc:oracle:thin:@localhost:1521:XE";
            String strUserID = "loko_lab";
            String strPassword = "pass";
            Connection myConnection= DriverManager.getConnection(dbURL,strUserID,strPassword);

            Statement sqlStatement = myConnection.createStatement();
            String readRecordSQL = "select * from CUSTOMER";
            ResultSet rs = sqlStatement.executeQuery(readRecordSQL);
            while (rs.next()) {
                System.out.println(rs.getString(1) + ", " + rs.getString(2) + ", " +
                        rs.getString(3) + ", " + rs.getString(4)
                        + ", " + rs.getString(5));
            }
            rs.close();
            myConnection.close();

        } catch (Exception e) {
            System.out.println(e);
        }


    }
}
