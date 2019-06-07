package components.dialogs.reports;

import backend.MasterclassProvider;
import components.dialogs.InputDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MasterclassFilterPlayerDialog extends InputDialog {

    MasterclassProvider provider = new MasterclassProvider();

    public MasterclassFilterPlayerDialog() {
        int rating = Integer.parseInt(JOptionPane.showInputDialog(null, "Op welke minimale rating wilt u filtreren?"));

        try {
            ResultSet rs = provider.filterPlayersByRating(rating);

            ResultSetMetaData rsmd = rs.getMetaData();

            JTable tbl = new JTable();
            DefaultTableModel dtm = new DefaultTableModel(0, 0);

            String header[] = new String[rsmd.getColumnCount()];

            for(int i = 0; i < rsmd.getColumnCount(); i++) {
                header[i] = rsmd.getColumnName(i + 1);
            }

            dtm.setColumnIdentifiers(header);

            tbl.setModel(dtm);

            while (rs.next()) {
                dtm.addRow(new Object[] { rs.getObject(1).toString(), rs.getObject(2).toString(), rs.getObject(3).toString() });
            }

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout());
            JScrollPane sp = new JScrollPane(tbl);
            panel.add(sp);

            JDialog tableDialog = new JDialog();
            tableDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            tableDialog.setContentPane(panel);

            tableDialog.pack();
            tableDialog.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
