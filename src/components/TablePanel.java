package components;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

public class TablePanel extends JPanel {

    public TablePanel(TableModel model) {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.CYAN);

        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);

        JScrollPane tableWithScroll = new JScrollPane(table);
        tableWithScroll.setVisible(true);

        this.add(table.getTableHeader(), BorderLayout.PAGE_START);
        this.add(tableWithScroll, BorderLayout.CENTER);
    }
}
