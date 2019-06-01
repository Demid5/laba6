import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class MainForm extends JFrame{
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JScrollPane scrollPane1;
    private JScrollPane scrollPane2;
    private JScrollPane scrollPane3;
    private JScrollPane scrollPaneCustomer;
    private JScrollPane scrollPaneProduct;
    private JScrollPane scrollPane6;
    private JScrollPane scrollPane7;
    private JScrollPane scrollPane8;
    private JScrollPane scrollPane9;
    private JTable table1;
    private JTable table3;
    private JTable table2;
    private JTable tableCustomer;
    private JTable tableProduct;
    private JTable table6;
    private JTable table7;
    private JTable table8;
    private JTable table9;
    private JPanel panelCustomer;
    private JButton buttonCus;
    private JPanel panelProduct;
    private JButton buttonProduct;

    private void addRow(MyTableModel model, Object[] dat) {
        model.addRow(dat);
    }

    private void initTableData(MyTableModel model, ResultSet rs) {
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
    }

    private void initCustomer() {
        Object[] headers = {"pk", "Имя", "ИНН", "Адрес", "Телефон"};
        Object[][] data = {};
        MyTableModel model = new MyTableModel(data, headers);
        tableCustomer.setModel(model);

        DataBase db = DataBase.getInstance();
        ResultSet rs = db.executeRequest("select * from customer");
        initTableData(model, rs);

        tableCustomer.removeColumn(tableCustomer.getColumnModel().getColumn(0));
        tableCustomer.addMouseListener(new ExtMouseAdapter("CustomerDialog", 5));

        buttonCus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomerDialog dialog = new CustomerDialog((MyTableModel) tableCustomer.getModel());
            }
        });
    }

    private void initProduct() {
        Object[] headers = {"pk", "Название продукта", "Цена", "pk_measure", "Единица измерения"};
        Object[][] data = {};
        MyTableModel model = new MyTableModel(data, headers);
        tableProduct.setModel(model);

        DataBase db = DataBase.getInstance();
        ResultSet rs = db.executeRequest("select product.*, measure.name_m from product " +
                "inner join measure on measure.pk_measure = product.pk_measure");

        initTableData(model, rs);

        tableProduct.removeColumn(tableProduct.getColumnModel().getColumn(3));
        tableProduct.removeColumn(tableProduct.getColumnModel().getColumn(0));
        tableProduct.addMouseListener(new ExtMouseAdapter("ProductDialog", 5));

        buttonProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ProductDialog dialog = new ProductDialog((MyTableModel) tableProduct.getModel());
            }
        });
    }

    public MainForm() {
        initCustomer();
        initProduct();

        setContentPane(panel1);
        setSize(new Dimension(800, 600));
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


    }

}
