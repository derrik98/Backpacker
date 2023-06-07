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

    public void switchToAddItinerary(){
        this.catcher(() -> {
            stackScene.add("addIt");
            CliAddItineraryController cliAddItineraryController = new CliAddItineraryController();
            cliAddItineraryController.init();
        });
    }

    @Override
    protected void undo(Scanner scanner) {
        this.catcher(() -> {

            if (stackScene.size() > 1) {

                String from = stackScene.get(stackScene.size() - 2);
                switch (from) {
                    case "home" -> this.switchToHome(scanner);
                    case "result" -> this.switchToResult(scanner);
                    case "profile" -> this.switchToGuideDetails();
                    default -> System.out.println("Unable to undo");
                }
                stackScene.remove(stackScene.size() - 1);

            } else {
                this.switchToHome(scanner);
            }
        });
    }
}
