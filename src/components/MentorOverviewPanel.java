package components;

import backend.MentorProvider;
import components.panels.OverviewPanel;
import components.representation.GenericTableModel;
import components.representation.RepresentationBuilder;
import components.representation.Representor;
import models.Mentor;

import java.awt.*;
import java.sql.SQLException;

public class MentorOverviewPanel extends OverviewPanel {

    MentorProvider provider;
    Representor<Mentor> representor;
    GenericTableModel<Mentor> model;
    TablePanel tablePanel;

    public MentorOverviewPanel() {
        provider = new MentorProvider();

        representor = new RepresentationBuilder<Mentor>()
                .addColumn("Id", Mentor::getId)
                .addColumn("Naam", Mentor::getName)
                .addColumn("Telefoonnummer", Mentor::getPhoneNumber)
                .addColumn("Emailadres", Mentor::getEmail)
                .build();

        fetchMentorData();
        tablePanel = new TablePanel(model);
        add(tablePanel, BorderLayout.CENTER);
        createButtons();
    }

    private void fetchMentorData() {
        try {
            model = new GenericTableModel<>(provider.getAllMentors(), representor);
        } catch (SQLException exception) {
            exception.printStackTrace();
            model = new GenericTableModel<>(representor);
        }
    }

    @Override
    protected void createButtons() {

    }
}
