import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;

public class MainForm extends JFrame {
    private JPanel panel1;
    private JTable table1;
    private JScrollPane scroll;

    public MainForm() {


        Object[] headers = {"name", "address", "telephone"};
        Object[][] data = {};


        DefaultTableModel model = new DefaultTableModel(data, headers);
        table1 = new JTable(model);

        //table1.setVisible(true);
        scroll = new JScrollPane(table1);
        //scroll.add(table1);

        panel1.add(scroll, BorderLayout.CENTER);


        setContentPane(panel1);
        setSize(new Dimension(800, 600));
        //table1.addColumn();

        /*DefaultTableModel tableModel = (DefaultTableModel) table1.getModel();
        tableModel.setColumnIdentifiers(headers);
        tableModel.addColumn("Имя");
        tableModel.addColumn("Адрес");
        tableModel.addColumn("Телефон");*/
        //Object[][] data = {{"имя", "адрес", "телефон"}};


        /*JTableHeader header = new JTableHeader();
        table1.setTableHeader(header);
        tableModel.addRow(data);
        tableModel.addRow(data);*/



        setVisible(true);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void addRow(Object[] dat) {
        DefaultTableModel model = (DefaultTableModel) (table1.getModel());
        model.addRow(dat);
    }


}
