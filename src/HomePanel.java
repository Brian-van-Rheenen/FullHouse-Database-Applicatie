import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComponent;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

public class HomePanel extends JPanel {
    
    public HomePanel() {
        super(new GridLayout(1, 1));
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JComponent panel1 = makeTextPanel("Panel #1");
        tabbedPane.addTab("Gebruikers", null, panel1,
                "Lijst van gebruikers");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        JComponent panel2 = makeTextPanel("Panel #2");
        tabbedPane.addTab("Toernooien", null, panel2,
                "Lijst van toernooien");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        JComponent panel3 = makeTextPanel("Panel #3");
        tabbedPane.addTab("Masterclasses", null, panel3,
                "Lijst van masterclasses");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        
        //Add the tabbed pane to this panel.
        add(tabbedPane);
        
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);

        // lijst

        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
}
