import java.sql.*;

public class Main {
    public static void main(String[] args) {

        /*try {
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
            System.out.println(e.toString());
        }*/

        //testForm m = new testForm();
        AnotherForm m = new AnotherForm();
        /*DataBase db = DataBase.getInstance();
        ResultSet rs = db.executeTest();
        try {
            while (rs.next()) {
                Object[] data = new Object[3];
                data[0] = rs.getString(2);
                data[1] = rs.getString(4);
                data[2] = rs.getString(5);
                m.addRow(data);
            }
            rs.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }*/

        //m.show();


    }
}
