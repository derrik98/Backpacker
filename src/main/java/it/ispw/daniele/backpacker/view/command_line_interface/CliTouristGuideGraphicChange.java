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
            stackScene.push("home");
            CliHomeController homeController = new CliHomeController();
            homeController.init(scanner);
        });
    }

    @Override
    protected void undo(Scanner scanner) {

    }

    public void switchToGuideDetails() {
        this.catcher(() -> {
            stackScene.push("home");
            CliGuideDetailsController cliGuideDetailsController = new CliGuideDetailsController();
            cliGuideDetailsController.init();
        });
    }

    public void switchToAddItinerary(){
        this.catcher(() -> {
            stackScene.push("addIt");
            CliAddItineraryController cliAddItineraryController = new CliAddItineraryController();
            cliAddItineraryController.init();
        });
    }
}
