package components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TablePanel extends JPanel {

    public TablePanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.CYAN);

        // Vul met query resultaat
        String[] columnNames = {"First Name",
                                "Last Name",
                                "Sport",
                                "# of Years",
                                "Vegetarian"};

        // Vul met query resultaat
        Object[][] data = {
                {"Kathy", "Smith",
                        "Snowboarding", 5, Boolean.FALSE},
                {"John", "Doe",
                        "Rowing", 3, Boolean.TRUE},
                {"Sue", "Black",
                        "Knitting", 2, Boolean.FALSE},
                {"Jane", "White",
                        "Speed reading", 20, Boolean.TRUE},
                {"Joe", "Brown",
                        "Pool", 10, Boolean.FALSE}
        };

        JTable table = new JTable(data, columnNames);
        table.setAutoCreateRowSorter(true);

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {

            // Are the cells editable?
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);

        this.add(table.getTableHeader(), BorderLayout.PAGE_START);
        this.add(table, BorderLayout.CENTER);
    }
}
