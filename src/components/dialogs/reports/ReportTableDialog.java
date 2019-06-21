package components.dialogs.reports;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * A JDialog with a JTable that dynamically adds columns and rows based on the ResultSet it receives.
 * The JDialog will automatically scale to the size of the JTable.
 */
public class ReportTableDialog extends JDialog{

    /**
     * The constructor for this JDialog
     * @param title String - The title that this JDialog will have.
     * @param data ResultSet - The data that the JTable needs to fill itself.
     */
    public ReportTableDialog(String title, ResultSet data) {
        try {
            JTable dataTable = new JTable();
            DefaultTableModel dtm = new DefaultTableModel(0, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            ResultSetMetaData ResultSetMetaData = data.getMetaData();

            String header[] = new String[ResultSetMetaData.getColumnCount()];

            // Set table columns
            for (int i = 0; i < ResultSetMetaData.getColumnCount(); i++) {
                header[i] = ResultSetMetaData.getColumnName(i + 1);
            }
            dtm.setColumnIdentifiers(header);

            int colCount = header.length;
            Object[] row = new Object[colCount];

            // Set table rows
            while (data.next()) {
                for(int i = 0; i < colCount; i++) {
                    row[i] = data.getObject(i + 1).toString();
                }
                dtm.addRow(row);
            }

            dataTable.setModel(dtm);

            // Set the table's preferred scrollable viewport. Add 200 to the width to make the rows readable.
            dataTable.setPreferredScrollableViewportSize(new Dimension(dataTable.getPreferredSize().width + 200, dataTable.getRowCount() * dataTable.getRowHeight()));

            JScrollPane sp = new JScrollPane(dataTable);

            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.add(sp);

            this.pack();

            this.setTitle(title);
            this.setLocationRelativeTo(null);
            this.setModalityType(ModalityType.APPLICATION_MODAL);
            this.setVisible(true);
        }
        catch (SQLException sql) {
            // handle SQL error
            sql.printStackTrace();
        }
    }
}
