import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
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

    public ExtMouseAdapter() {

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
                        Object[] data = new Object[3];
                        data[0] = ((JTable) owner).getModel().getValueAt(row, 0);
                        data[1] = ((JTable) owner).getModel().getValueAt(row, 1);
                        data[2] = ((JTable) owner).getModel().getValueAt(row, 2);
                        Constructor constructor = Class.forName("TestDialog").getConstructor(String.class, Object[].class);
                        System.out.println(constructor);
                        TestDialog dialog = (TestDialog) constructor.newInstance("wtf", data);
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