package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.utils.Roles;

import java.util.Scanner;

public class CliUserGraphicChange extends CliChangeTemplate {

    private static CliUserGraphicChange instance = null;

    private CliUserGraphicChange(){
        whoAmI = Roles.USER;
    }

    public static CliUserGraphicChange getInstance(){
        if(instance == null){
            instance = new CliUserGraphicChange();
        }
        return instance;
    }

    public  void switchToHome(Scanner scanner){
        this.catcher(() -> {
            stackScene.push("home");
            CliHomeController homeController = new CliHomeController();
            homeController.init(scanner);
        });
    }

    public  void switchToUserDetails(){
        this.catcher(() -> {
            stackScene.push("profile");
            CliUserDetailsController cliUserDetailsController = new CliUserDetailsController();
            cliUserDetailsController.init();
        });
    }

    public void undo(Scanner scanner){
        this.catcher(() -> {

            System.out.println(stackScene);

            if (stackScene.size() > 1) {

                System.out.println(stackScene);
                System.out.println(stackScene.get(stackScene.size() - 2));
                String from = stackScene.get(stackScene.size() - 2);
                switch (from) {
                    case "home" -> this.switchToHome(scanner);
                    case "result" -> this.switchToResult(scanner);
                    case "profile" -> this.switchToUserDetails();
                }
                stackScene.remove(stackScene.size() - 1);

            } else {
                this.switchToHome(scanner);
            }

        });
    }

}
