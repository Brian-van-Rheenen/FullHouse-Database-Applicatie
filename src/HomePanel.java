import backend.PlayerProvider;
import components.ParticipantOverviewPanel;
import components.UserOverviewPanel;

import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComponent;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

/**
 * This is the start panel with contains the tabbed pane (which in turn contains the different overview
 * panels).
 */
public class HomePanel extends JPanel {

    public HomePanel() throws SQLException {
        super(new GridLayout(1, 1));

        JTabbedPane tabbedPane = new JTabbedPane();

        UserOverviewPanel userOverviewPanel = new UserOverviewPanel();
        tabbedPane.addTab("Spelers", null, userOverviewPanel,
                "Lijst van spelers");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent tournamentPanel = createPanel("Panel #2");
        tabbedPane.addTab("Toernooien", null, tournamentPanel,
                "Lijst van toernooien");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        JComponent masterclassPanel = createPanel("Panel #3");
        tabbedPane.addTab("Masterclasses", null, masterclassPanel,
                "Lijst van masterclasses");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);


        tabbedPane.addTab("Toernooi deelnames", null, new ParticipantOverviewPanel(),
                "Overzicht van deelnames");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

        add(tabbedPane);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    
    protected JComponent createPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);

        // lijst

        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
}
