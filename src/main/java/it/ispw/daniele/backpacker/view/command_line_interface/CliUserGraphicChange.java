package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.utils.Roles;

import java.util.Scanner;

public class CliUserGraphicChange extends CliGuiChangeTemplate{

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
            CliHomeController homeController = new CliHomeController();
            homeController.init(scanner);
        });
    }

//    public void switchToResult(String country, String city, String address, String restaurant, String range) {
//        // Do nothing.
//    }
}
