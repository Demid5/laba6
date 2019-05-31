import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import javax.swing.JTable;
import javax.swing.Timer;

public class ExtMouseAdapter extends MouseAdapter {

    // Таймаут ожидания второго клика в миллисекундах.
    private int doubleClickDelay = 300;
    // Таймер ожидания второго клика.
    private Timer timer;
    private Object owner;
    private int click = 0;
    private int col = -1;
    private int row = -1;
    private String className;
    private int len;

    public ExtMouseAdapter(String className, int len) {

        this.className = className;
        this.len = len;

        ActionListener actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                timer.stop();
            }
        };
        owner = null;
        timer = new Timer(doubleClickDelay, actionListener);
        timer.setRepeats(false);
    }

    void setOwner(Object obj) {
        owner = obj;
        if (owner != null) {
            if (owner instanceof JTable) {
                JTable tab = (JTable) owner;
                row = tab.getSelectedRow();
                col = tab.getSelectedColumn();
            }
        }
    }

    boolean checkOwner(Object obj) {
        if (owner == obj) {
            if (owner instanceof JTable) {
                JTable tab = (JTable) owner;
                return tab.getSelectedRow() == row && tab.getSelectedColumn() == col;
            }
            return true;
        }
        return false;
    }

    void show(String s) {
        System.out.print(s);
        if (owner != null) {
            System.out.print(": " + owner.getClass().getName());
            if (owner instanceof JTable) {
                System.out.print(" row=" + row + " col=" + col);
            }
        } else {
            System.out.print(": null");
        }
        System.out.println();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!checkOwner(e.getSource())) {
            timer.stop();
            click = 0;
            setOwner(e.getSource());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (checkOwner(e.getSource())) {
            click++;
            if (click == 2 && timer.isRunning()) {
                show("double-click!");
                timer.stop();
                click = 0;
                try {
//                    AnotherForm af = (AnotherForm) Class.forName("AnotherForm").newInstance();
                    if (owner instanceof JTable) {

                        Object[] data = new Object[len];
                        /*data[0] = ((JTable) owner).getModel().getValueAt(row, 0);
                        data[1] = ((JTable) owner).getModel().getValueAt(row, 1);
                        data[2] = ((JTable) owner).getModel().getValueAt(row, 2);*/
                        for (int i = 0; i < len; i++) {
                            data[i] =  ((JTable) owner).getModel().getValueAt(row, i);
                        }

                        Constructor constructor =
                                Class.forName(className).getConstructor(Object[].class, MyTableModel.class, int.class);
                        Dialog dialog = (Dialog)
                                constructor.newInstance(data, ((JTable) owner).getModel(), row);
//                        TestDialog dialog = new TestDialog(data);
                    }
                }
                catch (Exception ee) {
                    System.out.println(ee.toString());
                }
                setOwner(null);
            } else {
                show("click!");
                click = 1;
                timer.start();
            }
        } else {
            // Что интересно - такое возможно только если перемещать выделенние
            // ячейки в сторону уменьшения номера колонки или строки.
            // Т.к. начало выделения - всегда верхний левый угол.
            show("out-drag-click!");
            timer.stop();
            click = 0;
            setOwner(null);
        }
    }
}