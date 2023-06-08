package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.utils.Roles;

import java.util.Scanner;

public class CliTouristGuideGraphicChange extends CliChangeTemplate {
    private static CliTouristGuideGraphicChange instance = null;

    private CliTouristGuideGraphicChange() {
        whoAmI = Roles.TouristGuide;
    }

    public static CliTouristGuideGraphicChange getInstance() {
        if (instance == null) {
            instance = new CliTouristGuideGraphicChange();
        }
        return instance;
    }

    @Override
    protected void switchToHome(Scanner scanner) {
        this.catcher(() -> {
            stackScene.add("home");
            CliHomeController homeController = new CliHomeController();
            homeController.init(scanner);
        });
    }

    public void switchToGuideDetails() {
        this.catcher(() -> {
            stackScene.add("home");
            CliGuideDetailsController cliGuideDetailsController = new CliGuideDetailsController();
            cliGuideDetailsController.init();
        });
    }

    public void switchToAddItinerary() {
        this.catcher(() -> {
            stackScene.add("addIt");
            CliAddItineraryController cliAddItineraryController = new CliAddItineraryController();
            cliAddItineraryController.init();
        });
    }

}
