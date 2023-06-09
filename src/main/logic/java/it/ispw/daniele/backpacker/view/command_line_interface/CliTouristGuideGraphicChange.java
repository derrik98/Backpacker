package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.utils.Roles;

import java.util.Scanner;

public class CliTouristGuideGraphicChange extends CliChangeTemplate {
    private static CliTouristGuideGraphicChange instance = null;

    private CliTouristGuideGraphicChange() {
        whoAmI = Roles.TOURIST_GUIDE;
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
            CliSearchController homeController = new CliSearchController();
            homeController.init(scanner);
        });
    }

    public void switchToGuideDetails(Scanner scanner) {
        this.catcher(() -> {
            stackScene.add("home");
            CliGuideDetailsController cliGuideDetailsController = new CliGuideDetailsController();
            cliGuideDetailsController.init(scanner);
        });
    }

    public void switchToAddItinerary(Scanner scanner) {
        this.catcher(() -> {
            stackScene.add("addIt");
            CliAddItineraryController cliAddItineraryController = new CliAddItineraryController();
            cliAddItineraryController.init(scanner);
        });
    }

}
