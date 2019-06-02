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
    private JScrollPane scrollPaneInvoice;
    private JTable table1;
    private JTable table3;
    private JTable table2;
    private JTable tableCustomer;
    private JTable tableProduct;
    private JTable table6;
    private JTable table7;
    private JTable table8;
    private JTable tableInvoice;
    private JPanel panelCustomer;
    private JButton buttonCus;
    private JPanel panelProduct;
    private JButton buttonProduct;
    private JButton buttonDeleteProduct;
    private JButton buttonDeleteCustomer;
    private JPanel panelInvoice;
    private JButton buttonAddInvoice;
    private JButton buttonDeleteInvoice;

    private void addRow(MyTableModel model, Object[] dat) {
        model.addRow(dat);
    }

    private void initTableData(MyTableModel model, ResultSet rs, int len) {
        try {
            while (rs.next()) {
                Object[] rowData = new Object[len];
                for (int i = 0; i < len; i++)
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
        final MyTableModel model = new MyTableModel(data, headers);
        tableCustomer.setModel(model);

        DataBase db = DataBase.getInstance();
        ResultSet rs = db.executeRequest("select * from customer");
        initTableData(model, rs, headers.length);

        tableCustomer.removeColumn(tableCustomer.getColumnModel().getColumn(0));
        tableCustomer.addMouseListener(new ExtMouseAdapter("CustomerDialog", 5));

        buttonCus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomerDialog dialog = new CustomerDialog((MyTableModel) tableCustomer.getModel());
            }
        });

        tableCustomer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        buttonDeleteCustomer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = tableCustomer.getSelectedRow();
                String pk = (String) model.getValueAt(row, 0);
                DataBase.getInstance().executeRequest("delete from customer where pk_customer = " + pk);
                model.removeRow(row);
            }
        });
    }

    private void initProduct() {
        Object[] headers = {"pk", "Название продукта", "Цена", "pk_measure", "Единица измерения"};
        Object[][] data = {};
        final MyTableModel model = new MyTableModel(data, headers);
        tableProduct.setModel(model);

        DataBase db = DataBase.getInstance();
        ResultSet rs = db.executeRequest("select product.*, measure.name_m from product " +
                "inner join measure on measure.pk_measure = product.pk_measure");

        initTableData(model, rs, headers.length);

        tableProduct.removeColumn(tableProduct.getColumnModel().getColumn(3));
        tableProduct.removeColumn(tableProduct.getColumnModel().getColumn(0));
        tableProduct.addMouseListener(new ExtMouseAdapter("ProductDialog", 5));

        buttonProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ProductDialog dialog = new ProductDialog((MyTableModel) tableProduct.getModel());
            }
        });
        tableProduct.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        buttonDeleteProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = tableProduct.getSelectedRow();
                String pk = (String) model.getValueAt(row, 0);
                DataBase.getInstance().executeRequest("delete from product where pk_product = " + pk);
                model.removeRow(row);
            }
        });
    }

    private void initInvoice() {
        Object[] headers = {"pk", "Номер накладной", "Дата", "pk_type", "pk_storekeeper", "pk_customer",
        "Тип накладой", "Кладовщик", "Покупатель"};
        Object[][] data = {};
        final MyTableModel model = new MyTableModel(data, headers);
        tableInvoice.setModel(model);

        DataBase db = DataBase.getInstance();
        ResultSet rs = db.executeRequest("select invoice.*, i_type.type_name, keeper.full_name, cust.full_name" +
                " from invoice" +
                " inner join invoice_type i_type on invoice.pk_invoice_type = i_type.pk_invoice_type" +
                " inner join storekeeper keeper on invoice.pk_storekeeper = keeper.pk_storekeeper" +
                " inner join customer cust on invoice.pk_customer = cust.pk_customer");

        initTableData(model, rs, headers.length);
        tableInvoice.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableInvoice.removeColumn(tableInvoice.getColumnModel().getColumn(5));
        tableInvoice.removeColumn(tableInvoice.getColumnModel().getColumn(4));
        tableInvoice.removeColumn(tableInvoice.getColumnModel().getColumn(3));
        tableInvoice.removeColumn(tableInvoice.getColumnModel().getColumn(0));
        tableInvoice.addMouseListener(new ExtMouseAdapter("InvoiceDialog", headers.length));

        buttonAddInvoice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InvoiceDialog dialog = new InvoiceDialog((MyTableModel) tableInvoice.getModel());
            }
        });


        buttonDeleteInvoice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = tableInvoice.getSelectedRow();
                String pk = (String) model.getValueAt(row, 0);
                DataBase.getInstance().executeRequest("delete from invoice where pk_invoice = " + pk);
                model.removeRow(row);
            }
        });
    }

    public MainForm() {
        initCustomer();
        initProduct();
        initInvoice();

        setContentPane(panel1);
        setSize(new Dimension(800, 600));
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

}
