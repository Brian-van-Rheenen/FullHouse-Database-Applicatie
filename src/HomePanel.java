import components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;


/**
 * This is the start panel with contains the tabbed pane (which in turn contains the different overview
 * panels).
 */
public class HomePanel extends JPanel {

    public HomePanel() {
        super(new GridLayout(1, 1));

        JTabbedPane tabbedPane = new JTabbedPane();

        PlayerOverviewPanel playerOverviewPanel = new PlayerOverviewPanel();

        tabbedPane.addTab("Spelers", null, playerOverviewPanel,
                "Lijst van spelers");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent tournamentPanel = new TournamentOverviewPanel();
        tabbedPane.addTab("Toernooien", null, tournamentPanel,
                "Lijst van toernooien");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        JComponent masterclassPanel = new MasterclassOverviewPanel();
        tabbedPane.addTab("Masterclasses", null, masterclassPanel,
                "Lijst van masterclasses");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        JComponent wellKnownPlayers = new MentorOverviewPanel();
        tabbedPane.addTab("Bekende spelers", null, wellKnownPlayers,
                "Lijst van bekende spelers");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_4);

        tabbedPane.addTab("Deelnames", null, new ParticipantOverviewPanel(),
                "Overzicht van deelnames");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_5);

        add(tabbedPane);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    
    protected JComponent createPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);

        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
}
