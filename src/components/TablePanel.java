package components;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

public class TablePanel extends JPanel {
    private JTable table;

    public TablePanel(TableModel model) {
        this.setLayout(new BorderLayout());

        table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableWithScroll = new JScrollPane(table);
        tableWithScroll.setVisible(true);

        this.add(table.getTableHeader(), BorderLayout.PAGE_START);
        this.add(tableWithScroll, BorderLayout.CENTER);
    }

    public int getColumnCount() {
        return table.getColumnCount();
    }

    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    public int[] getSelectedRows() {
        return table.getSelectedRows();
    }

    public int getSelectedColumn() {
        return table.getSelectedColumn();
    }
}
