import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class AnotherForm extends JFrame {

    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JPanel innerPanel1;
    private JPanel innerPanel2;
    private JPanel innerPanel3;

    public AnotherForm() {

        super("Тестовое окно");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font font = new Font("Verdana", Font.PLAIN, 12);
        //final JTabbedPane tabbedPane1 = new JTabbedPane();
        tabbedPane1.setFont(font);



        setPreferredSize(new Dimension(800, 600));
        pack();
        setContentPane(panel1);
        setVisible(true);
    }

}