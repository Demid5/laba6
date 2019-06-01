import javax.swing.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class ProductDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textAreaPK;
    private JTextArea textAreaName;
    private JTextArea textAreaCost;
    private JComboBox comboBoxMeasureName;
    private JTextArea textAreaMeasurePK;
    private MyTableModel model;
    private int row;
    private HashMap<String, Integer> textToValue;
    //private HashMap<String, Integer> textToPosition;
    private HashMap<Integer,Integer> valueToPosition;

    private void initComboBox() {
        DataBase db = DataBase.getInstance();
        ResultSet rs = db.executeRequest("select * from measure");
        try {
            int i = 0;
            textToValue = new HashMap<String, Integer>();
            //textToPosition = new HashMap<String, Integer>();
            valueToPosition = new HashMap<Integer, Integer>();
            while (rs.next()) {
                textToValue.put(rs.getString(2), rs.getInt(1));
                //textToPosition.put(rs.getString(2), i);
                valueToPosition.put(rs.getInt(1), i++);
                comboBoxMeasureName.addItem(rs.getString(2));
            }
            rs.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public ProductDialog(Object[] data, MyTableModel model, int row) {
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

        textAreaPK.setText((String) data[0]);
        textAreaName.setText((String) data[1]);
        textAreaCost.setText((String) data[2]);
        textAreaMeasurePK.setText((String) data[3]);

        initComboBox();
        comboBoxMeasureName.setSelectedIndex(valueToPosition.get(Integer.valueOf(textAreaMeasurePK.getText())));

        comboBoxMeasureName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textAreaMeasurePK.setText(textToValue.get(comboBoxMeasureName.getSelectedItem()).toString());
            }
        });

        pack();
        setVisible(true);
    }

    public ProductDialog(MyTableModel model) {
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

        initComboBox();

        comboBoxMeasureName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textAreaMeasurePK.setText(textToValue.get(comboBoxMeasureName.getSelectedItem()).toString());
            }
        });

        pack();
        setVisible(true);
    }


    private void onOK() {
        try {
            DataBase db = DataBase.getInstance();
            PreparedStatement statement;
            if (!textAreaPK.getText().equals("")) {
                statement = db.prepareStatement("update product set name_p = ?, price = ?," +
                        " pk_measure = ? where pk_product = ?");
                statement.setString(1, textAreaName.getText());
                statement.setString(2, textAreaCost.getText());
                statement.setString(3, textAreaMeasurePK.getText());
                statement.setString(4, textAreaPK.getText());
                statement.executeQuery();
                statement.close();

                model.setValueAt(textAreaName.getText(), row, 1);
                model.setValueAt(textAreaCost.getText(), row, 2);
                model.setValueAt(textAreaMeasurePK.getText(), row, 3);
                model.setValueAt(comboBoxMeasureName.getSelectedItem(), row, 4);

            } else {
                ResultSet rs = db.executeRequest("select product_seq.nextval from dual");
                rs.next();
                int pk = rs.getInt(1);
                rs.close();
                statement = db.prepareStatement("insert into product(pk_product, name_p, price, pk_measure)" +
                        " values(?, ?, ?, ?)");
                statement.setInt(1, pk);
                statement.setString(2, textAreaName.getText());
                statement.setString(3, textAreaCost.getText());
                statement.setString(4, textAreaMeasurePK.getText());
                statement.executeQuery();
                statement.close();

                Object[] data = new Object[5];
                data[0] = String.valueOf(pk);
                data[1] = textAreaName.getText();
                data[2] = textAreaCost.getText();
                data[3] = textAreaMeasurePK.getText();
                data[4] = comboBoxMeasureName.getSelectedItem();

                model.addRow(data);
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
