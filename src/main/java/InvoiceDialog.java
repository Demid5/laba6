import oracle.jdbc.proxy.annotation.Pre;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
    private JTextField textFieldAmount;
    private JTextField textFieldPrice;
    private JComboBox comboBoxProduct;
    private JButton buttonAddLine;
    private JButton buttonPrev;
    private JButton buttonNext;
    private JButton buttonRemoveLine;

    private MyTableModel model;
    private int row;
    private int pk, pkType, pkSorekeeper, pkCustomer;
    private HashMap<String, Integer> typeTextToValue;
    private HashMap<Integer,Integer> typeValueToPosition;
    private HashMap<String, Integer> keeperTextToValue;
    private HashMap<Integer,Integer> keeperValueToPosition;
    private HashMap<String, Integer> cusTextToValue;
    private HashMap<Integer,Integer> cusValueToPosition;

    private ArrayList<Integer> linePk, lineAmount, pkProduct;
    private ArrayList<Double> linePrice;
    private ArrayList<Boolean> lineExist;
    private HashMap<String, Integer> lineTextToValue;
    private HashMap<Integer,Integer> lineValueToPosition;
    private int current = 0, amountOfLines = 0;

    private void save() {
        linePrice.set(current, Double.valueOf(textFieldPrice.getText()));
        lineAmount.set(current, Integer.valueOf(textFieldAmount.getText()));
        pkProduct.set(current, lineTextToValue.get(comboBoxProduct.getSelectedItem()));
    }

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
        lineTextToValue = new HashMap<String, Integer>();
        lineValueToPosition = new HashMap<Integer, Integer>();

        initComboBox("select * from storekeeper", keeperTextToValue, keeperValueToPosition, comboBoxKeeper);
        initComboBox("select * from invoice_type", typeTextToValue, typeValueToPosition, comboBoxType);
        initComboBox("select pk_customer, full_name from customer", cusTextToValue,
                cusValueToPosition, comboBoxCustomer);
        initComboBox("select pk_product, name_p from product", lineTextToValue,
                lineValueToPosition, comboBoxProduct);


        buttonNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
                current++;
                if (current == amountOfLines)
                    current = 0;
                textFieldAmount.setText(String.valueOf(lineAmount.get(current)));
                textFieldPrice.setText(String.valueOf(linePrice.get(current)));
                comboBoxProduct.setSelectedIndex(lineValueToPosition.get(pkProduct.get(current)));
            }
        });
        buttonPrev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
                current--;
                if (current == -1)
                    current = amountOfLines - 1;
                textFieldAmount.setText(String.valueOf(lineAmount.get(current)));
                textFieldPrice.setText(String.valueOf(linePrice.get(current)));
                comboBoxProduct.setSelectedIndex(lineValueToPosition.get(pkProduct.get(current)));
            }
        });
        buttonAddLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
                current = amountOfLines++;
                textFieldAmount.setText("0");
                textFieldPrice.setText("0");
                lineAmount.add(0);
                lineExist.add(false);
                linePrice.add(0.0);
                linePk.add(0);
                pkProduct.add(lineTextToValue.get(comboBoxProduct.getSelectedItem()));
            }
        });
        buttonRemoveLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (amountOfLines > 1) {
                    if (lineExist.get(current)) {
                        int deletedPK = linePk.get(current);
                        lineExist.remove(current);
                        linePk.remove(current);
                        linePrice.remove(current);
                        lineAmount.remove(current);
                        pkProduct.remove(current);
                        DataBase.getInstance().executeRequest("delete from invoce_line where pk_invoice_line = "
                                + deletedPK);
                    }
                    else {
                        lineExist.remove(current);
                        linePk.remove(current);
                        linePrice.remove(current);
                        lineAmount.remove(current);
                        pkProduct.remove(current);
                    }
                    amountOfLines--;
                    if (current == amountOfLines)
                        current--;
                    textFieldAmount.setText(String.valueOf(lineAmount.get(current)));
                    textFieldPrice.setText(String.valueOf(linePrice.get(current)));
                    comboBoxProduct.setSelectedIndex(lineValueToPosition.get(pkProduct.get(current)));
                }
            }
        });
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
        textFieldNumber.setText((String) data[1]);
        if (data[0] instanceof Integer)
            pk = (Integer)data[0];
        else
            pk = Integer.valueOf((String) data[0]);
        if (data[3] instanceof Integer)
            pkType = (Integer) data[3];
        else
            pkType = Integer.valueOf((String) data[3]);
        if (data[5] instanceof Integer)
            pkCustomer = (Integer)data[5];
        else
            pkCustomer = Integer.valueOf((String) data[5]);
        if (data[4] instanceof Integer)
            pkSorekeeper = (Integer)data[4];
        else
            pkSorekeeper = Integer.valueOf((String) data[4]);
        formattedTextFieldDate.setText((String) data[2]);

        comboBoxCustomer.setSelectedIndex(cusValueToPosition.get(pkCustomer));
        comboBoxKeeper.setSelectedIndex(keeperValueToPosition.get(pkSorekeeper));
        comboBoxType.setSelectedIndex(typeValueToPosition.get(pkType));

        linePk = new ArrayList<Integer>();
        lineAmount = new ArrayList<Integer>();
        linePrice = new ArrayList<Double>();
        lineExist = new ArrayList<Boolean>();
        pkProduct = new ArrayList<Integer>();

        ResultSet rs = DataBase.getInstance().executeRequest("select * from invoce_line where pk_invoice = "
                + pk);
        try {
            while (rs.next()) {
                linePk.add(rs.getInt(1));
                lineAmount.add(rs.getInt(2));
                lineExist.add(true);
                linePrice.add(rs.getDouble(3));
                pkProduct.add(rs.getInt(4));
                amountOfLines++;
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }

        textFieldAmount.setText(String.valueOf(lineAmount.get(0)));
        textFieldPrice.setText(String.valueOf(linePrice.get(0)));
        comboBoxProduct.setSelectedIndex(lineValueToPosition.get(pkProduct.get(0)));


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
        pkType = typeTextToValue.get(comboBoxType.getSelectedItem());
        pkCustomer = cusTextToValue.get(comboBoxCustomer.getSelectedItem());

        linePk = new ArrayList<Integer>();
        linePk.add(0);
        lineAmount = new ArrayList<Integer>();
        lineAmount.add(0);
        linePrice = new ArrayList<Double>();
        linePrice.add(0.0);
        lineExist = new ArrayList<Boolean>();
        lineExist.add(false);
        pkProduct = new ArrayList<Integer>();
        pkProduct.add(lineTextToValue.get(comboBoxProduct.getSelectedItem()));

        amountOfLines = 1;

        pack();
        setVisible(true);
    }


    private void onOK() {
        save();
        try {
            if (pk != -1) {

                PreparedStatement statement = DataBase.getInstance().prepareStatement("update invoice set " +
                        "invoice_number = ?, invoice_date = ?" +
                        ", pk_invoice_type = ?, pk_storekeeper = ?, pk_customer = ? where pk_invoice = ?");
                statement.setString(1, textFieldNumber.getText());
                statement.setDate(2, Date.valueOf(formattedTextFieldDate.getText()));
                statement.setInt(3, typeTextToValue.get(comboBoxType.getSelectedItem()));
                statement.setInt(5, cusTextToValue.get(comboBoxCustomer.getSelectedItem()));
                statement.setInt(4, keeperTextToValue.get(comboBoxKeeper.getSelectedItem()));
                statement.setInt(6, pk);
                statement.execute();
                statement.close();
                Object[] headers = {"pk", "Номер накладной", "Дата", "pk_type", "pk_storekeeper", "pk_customer",
                        "Тип накладой", "Кладовщик", "Покупатель"};
                model.setValueAt(textFieldNumber.getText(), row, 1);
                model.setValueAt(formattedTextFieldDate.getText(), row, 2);
                model.setValueAt(typeTextToValue.get(comboBoxType.getSelectedItem()), row, 3);
                model.setValueAt(keeperTextToValue.get(comboBoxKeeper.getSelectedItem()), row, 4);
                model.setValueAt(cusTextToValue.get(comboBoxCustomer.getSelectedItem()), row, 5);
                model.setValueAt(comboBoxType.getSelectedItem(), row, 6);
                model.setValueAt(comboBoxKeeper.getSelectedItem(), row, 7);
                model.setValueAt(comboBoxCustomer.getSelectedItem(), row, 8);

            } else {
                ResultSet rs = DataBase.getInstance().executeRequest("select INVOICE_SEQ.nextval from dual");
                rs.next();
                pk = rs.getInt(1);
                rs.close();
                PreparedStatement statement = DataBase.getInstance().prepareStatement("insert into " +
                        "invoice(pk_invoice, invoice_number, invoice_date, pk_invoice_type, pk_storekeeper, " +
                        "pk_customer) values(?, ?, ?, ?, ?, ?)");
                statement.setString(2, textFieldNumber.getText());
                statement.setDate(3, Date.valueOf(formattedTextFieldDate.getText()));
                statement.setInt(4, typeTextToValue.get(comboBoxType.getSelectedItem()));
                statement.setInt(6, cusTextToValue.get(comboBoxCustomer.getSelectedItem()));
                statement.setInt(5, keeperTextToValue.get(comboBoxKeeper.getSelectedItem()));
                statement.setInt(1, pk);
                statement.execute();
                statement.close();

                Object data[] = new Object[9];
                data[0] = pk;
                data[1] = textFieldNumber.getText();
                data[2] = formattedTextFieldDate.getText();
                data[3] = typeTextToValue.get(comboBoxType.getSelectedItem());
                data[4] = keeperTextToValue.get(comboBoxKeeper.getSelectedItem());
                data[5] = cusTextToValue.get(comboBoxCustomer.getSelectedItem());
                data[6] = comboBoxType.getSelectedItem();
                data[7] = comboBoxKeeper.getSelectedItem();
                data[8] = comboBoxCustomer.getSelectedItem();
                model.addRow(data);
            }

            for (int i = 0; i < amountOfLines; i++) {
                if (lineExist.get(i)) {
                    PreparedStatement statement = DataBase.getInstance().prepareStatement("update invoce_line " +
                            "set amount = ?, price = ?, pk_product = ? where pk_invoice_line = ?");
                    statement.setInt(4, linePk.get(i));
                    statement.setInt(1, lineAmount.get(i));
                    statement.setDouble(2, linePrice.get(i));
                    statement.setInt(3, pkProduct.get(i));
                    statement.execute();
                    statement.close();
                }
                else {
                    PreparedStatement statement = DataBase.getInstance().prepareStatement("insert into " +
                            "invoce_line(amount, price, pk_product, pk_invoice) values(?, ?, ?, ?)");
                    statement.setInt(1, lineAmount.get(i));
                    statement.setDouble(2, linePrice.get(i));
                    statement.setInt(3, pkProduct.get(i));
                    statement.setInt(4, pk);
                    statement.execute();
                    statement.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
