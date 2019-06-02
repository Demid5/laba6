import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class InvoiceDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldNumber;
    private JFormattedTextField formattedTextFieldDate;
    private JComboBox comboBoxType;
    private JComboBox comboBoxKeeper;
    private JComboBox comboBoxCustomer;

    private MyTableModel model;
    private int row;
    private int pk, pkType, pkSorekeeper, pkCustomer;
    private HashMap<String, Integer> typeTextToValue;
    private HashMap<Integer,Integer> typeValueToPosition;
    private HashMap<String, Integer> keeperTextToValue;
    private HashMap<Integer,Integer> keeperValueToPosition;
    private HashMap<String, Integer> cusTextToValue;
    private HashMap<Integer,Integer> cusValueToPosition;

    private void initComboBox(String query, HashMap textToValue, HashMap valueToPosition, JComboBox comboBox) {
        DataBase db = DataBase.getInstance();
        ResultSet rs = db.executeRequest(query);
        try {
            int i = 0;
            while (rs.next()) {
                textToValue.put(rs.getString(2), rs.getInt(1));
                valueToPosition.put(rs.getInt(1), i++);
                comboBox.addItem(rs.getString(2));
            }
            rs.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void defaultInit() {
        try {
            MaskFormatter dateMask = new MaskFormatter("####-##-##");
            dateMask.install(formattedTextFieldDate);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        typeTextToValue = new HashMap<String, Integer>();
        typeValueToPosition = new HashMap<Integer, Integer>();
        keeperTextToValue = new HashMap<String, Integer>();
        keeperValueToPosition = new HashMap<Integer, Integer>();
        cusTextToValue = new HashMap<String, Integer>();
        cusValueToPosition = new HashMap<Integer, Integer>();

        initComboBox("select * from storekeeper", keeperTextToValue, keeperValueToPosition, comboBoxKeeper);
        initComboBox("select * from invoice_type", typeTextToValue, typeValueToPosition, comboBoxType);
        initComboBox("select pk_customer, full_name from customer", cusTextToValue,
                cusValueToPosition, comboBoxCustomer);

    }

    public InvoiceDialog(Object[] data, MyTableModel model, int row) {
        this.model = model;
        this.row = row;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        defaultInit();
        Object[] headers = {"pk", "Номер накладной", "Дата", "pk_type", "pk_storekeeper", "pk_customer",
                "Тип накладой", "Кладовщик", "Покупатель"};
        textFieldNumber.setText((String) data[1]);
        pk = Integer.valueOf((String) data[0]);
        pkType = Integer.valueOf((String) data[3]);
        pkCustomer = Integer.valueOf((String) data[5]);
        pkSorekeeper = Integer.valueOf((String) data[4]);
        formattedTextFieldDate.setText((String) data[2]);

        comboBoxCustomer.setSelectedIndex(cusValueToPosition.get(pkCustomer));
        comboBoxKeeper.setSelectedIndex(keeperValueToPosition.get(pkSorekeeper));
        comboBoxType.setSelectedIndex(typeValueToPosition.get(pkType));

        pack();
        setVisible(true);
    }

    public InvoiceDialog(MyTableModel model) {

        this.model = model;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        defaultInit();
        pk = -1;
        pkSorekeeper = keeperTextToValue.get(comboBoxKeeper.getSelectedItem());
        pkType = keeperTextToValue.get(comboBoxKeeper.getSelectedItem());
        pkCustomer = keeperTextToValue.get(comboBoxKeeper.getSelectedItem());

        pack();
        setVisible(true);
    }

    /*public InvoiceDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setVisible(true);
    }*/

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
