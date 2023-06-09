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
            stackScene.add("search");
            CliSearchController searchController = new CliSearchController();
            searchController.init(scanner);
        });
    }

    public  void switchToUserDetails(Scanner scanner){
        this.catcher(() -> {
            stackScene.add("profile");
            CliUserDetailsController cliUserDetailsController = new CliUserDetailsController();
            cliUserDetailsController.init(scanner);
        });
    }
}