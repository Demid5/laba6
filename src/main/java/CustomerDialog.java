import javax.swing.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textAreaPK;
    private JTextArea textAreaName;
    private JTextArea textAreaTIN;
    private JTextArea textAreaAddress;
    private JTextArea textAreaTelephone;
    private MyTableModel model;
    private int row;

    public CustomerDialog(Object[] data, MyTableModel model, int row) {
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
        textAreaTIN.setText((String) data[2]);
        textAreaAddress.setText((String) data[3]);
        textAreaTelephone.setText((String) data[4]);

        pack();
        setVisible(true);
    }

    public CustomerDialog(MyTableModel model) {
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

        pack();
        setVisible(true);
    }

    private void onOK() {
        try {
            DataBase db = DataBase.getInstance();
            PreparedStatement statement;
            if (!textAreaPK.getText().equals("")) {
                statement = db.prepareStatement("update customer set full_name = ?, tin = ?," +
                        " address = ?, telephone_number = ? where pk_customer = ?");
                statement.setString(1, textAreaName.getText());
                statement.setString(2, textAreaTIN.getText());
                statement.setString(3, textAreaAddress.getText());
                statement.setString(4, textAreaTelephone.getText());
                statement.setString(5, textAreaPK.getText());
                statement.executeQuery();
                statement.close();
                model.setValueAt(textAreaName.getText(), row, 1);
                model.setValueAt(textAreaTIN.getText(), row, 2);
                model.setValueAt(textAreaAddress.getText(), row, 3);
                model.setValueAt(textAreaTelephone.getText(), row, 4);
            }
            else {
                ResultSet rs = db.executeRequest("select customer_seq.nextval from dual");
                rs.next();
                int pk = rs.getInt(1);
                rs.close();
                statement = db.prepareStatement("insert into customer(pk_customer, full_name, tin, address, telephone_number)" +
                        " values(?, ?, ?, ?, ?)");
                statement.setInt(1, pk);
                statement.setString(2, textAreaName.getText());
                statement.setString(3, textAreaTIN.getText());
                statement.setString(4, textAreaAddress.getText());
                statement.setString(5, textAreaTelephone.getText());
                statement.executeQuery();
                statement.close();

                Object[] data = new Object[5];
                data[0] = String.valueOf(pk);
                data[1] = textAreaName.getText();
                data[2] = textAreaTIN.getText();
                data[3] = textAreaAddress.getText();
                data[4] = textAreaTelephone.getText();

                model.addRow(data);
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    /*public static void main(String[] args) {
        CustomerDialog dialog = new CustomerDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }*/
}
