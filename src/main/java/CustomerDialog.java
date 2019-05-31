import javax.swing.*;
import java.awt.event.*;
import java.sql.PreparedStatement;

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

    private void onOK() {
        try {
            DataBase db = DataBase.getInstance();
            PreparedStatement statement = db.prepareStatement("update customer set full_name = ?, tin = ?," +
                    " address = ?, telephone_number = ? where pk_customer = ?");
            statement.setString(1, textAreaName.getText());
            statement.setString(3, textAreaAddress.getText());
            statement.setString(2, textAreaTIN.getText());
            statement.setString(4, textAreaTelephone.getText());
            statement.setString(5, textAreaPK.getText());
            statement.executeQuery();
            statement.close();
            model.setValueAt(textAreaName.getText(), row, 1);
            model.setValueAt(textAreaTIN.getText(), row, 2);
            model.setValueAt(textAreaAddress.getText(), row, 3);
            model.setValueAt(textAreaTelephone.getText(), row, 4);
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
