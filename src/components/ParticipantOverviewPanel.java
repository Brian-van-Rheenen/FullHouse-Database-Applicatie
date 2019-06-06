package components;

import backend.DataGetter;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ParticipantOverviewPanel extends JPanel {

    private JComboBox <String> toernooiBox = new JComboBox<>();
    private DataGetter dataGetter;
    private DefaultTableModel tableModel = new DefaultTableModel();

    public ParticipantOverviewPanel(DataGetter dataGetter) {
        this.dataGetter = dataGetter;
    }




}
