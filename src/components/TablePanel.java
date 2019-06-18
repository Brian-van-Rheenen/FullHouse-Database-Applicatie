package components;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Container for a {@link JTable}
 */
public class TablePanel extends JPanel {
    private JTable table;

    public TablePanel(TableModel model) {
        this.setLayout(new BorderLayout());

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableWithScroll = new JScrollPane(table);
        tableWithScroll.setVisible(true);

        this.add(table.getTableHeader(), BorderLayout.PAGE_START);
        this.add(tableWithScroll, BorderLayout.CENTER);
    }

    /**
     * Invokes {@link JTable#getColumnCount()}
     * @return the result of {@link JTable#getColumnCount()}
     */
    public int getColumnCount() {
        return table.getColumnCount();
    }

    /**
     * Invokes {@link JTable#getSelectedRow()}
     * @return the result of {@link JTable#getSelectedRow()}
     */
    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    /**
     * Invokes {@link JTable#getSelectedRows()}
     * @return the result of {@link JTable#getSelectedRows()}
     */
    public int[] getSelectedRows() {
        return table.getSelectedRows();
    }

    /**
     * Invokes {@link JTable#getSelectedColumn()}
     * @return the result of {@link JTable#getSelectedColumn()}
     */
    public int getSelectedColumn() {
        return table.getSelectedColumn();
    }

    /**
     * Get the model of the current table
     * @return The {@link TableModel} of the table
     */
    public TableModel getModel() {
        return table.getModel();
    }

    /**
     * Adds a selection listener to the {@link JTable}
     * @param listener the {@link ListSelectionListener} to add
     */
    public void addSelectionListener(ListSelectionListener listener) {
        ListSelectionModel selectionModel = table.getSelectionModel();
        if(selectionModel != null) {
            selectionModel.addListSelectionListener(listener);
        }
    }
}
