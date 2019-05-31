import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public MyTableModel(Object[][] o1, Object[] o2) {
        super(o1, o2);
    }
}