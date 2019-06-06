package components.dialogs;

import javax.swing.*;
import java.awt.*;

public class InputDialog extends Component {
    /**
     * Find the parent JOptionPane. This keeps looping until it finds the parent
     * @param parent the component of which you need to know the parent
     * @return the parent JOptionPane
     */
    protected JOptionPane getOptionPane(JComponent parent) {
        JOptionPane pane;
        if (!(parent instanceof JOptionPane)) {
            pane = getOptionPane((JComponent)parent.getParent());
        } else {
            pane = (JOptionPane) parent;
        }
        return pane;
    }
}
