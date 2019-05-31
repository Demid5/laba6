import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class MainForm extends JFrame{
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JScrollPane scrollPane1;
    private JScrollPane scrollPane2;
    private JScrollPane scrollPane3;
    private JScrollPane scrollPaneCustomer;
    private JScrollPane scrollPane5;
    private JScrollPane scrollPane6;
    private JScrollPane scrollPane7;
    private JScrollPane scrollPane8;
    private JScrollPane scrollPane9;
    private JTable table1;
    private JTable table3;
    private JTable table2;
    private JTable tableCustomer;
    private JTable table5;
    private JTable table6;
    private JTable table7;
    private JTable table8;
    private JTable table9;

    private void addRow(MyTableModel model, Object[] dat) {
        model.addRow(dat);
    }

    private void initCustomer() {
        Object[] headers = {"pk", "Имя", "ИНН", "Адрес", "Телефон"};
        Object[][] data = {};
        MyTableModel model = new MyTableModel(data, headers);
        tableCustomer.setModel(model);

        DataBase db = DataBase.getInstance();
        ResultSet rs = db.executeRequest("select * from customer");
        try {
            while (rs.next()) {
                Object[] rowData = new Object[5];
                for (int i = 0; i < 5; i++)
                    rowData[i] = rs.getString(i + 1);
                addRow(model, rowData);
            }
            rs.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        tableCustomer.removeColumn(tableCustomer.getColumnModel().getColumn(0));
        tableCustomer.addMouseListener(new ExtMouseAdapter("CustomerDialog", 5));

    }

    public MainForm() {
        initCustomer();

        setContentPane(panel1);
        setSize(new Dimension(800, 600));
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

}
