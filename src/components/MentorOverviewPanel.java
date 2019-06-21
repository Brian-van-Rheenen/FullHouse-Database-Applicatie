package components;

import backend.MentorProvider;
import components.panels.OverviewPanel;

public class MentorOverviewPanel extends OverviewPanel {

    MentorProvider provider;

    public MentorOverviewPanel() {
        provider = new MentorProvider();
    }

    @Override
    protected void createButtons() {

    }
}
