import javax.swing.*;
import java.awt.*;

public class Application {

    private JFrame window = new JFrame();

    public Application()  {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(() -> {
            //Turn off metal's use of bold fonts
            UIManager.put("swing.boldMetal", Boolean.FALSE);

            window.setSize(1080, 720);
            window.setMinimumSize(new Dimension(1080, 720));
            window.setLocationRelativeTo(null);
            window.setTitle("FullHouse Database Applicatie");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            window.add(new HomePanel(), BorderLayout.CENTER);
            window.setVisible(true);
        });
    }
}
