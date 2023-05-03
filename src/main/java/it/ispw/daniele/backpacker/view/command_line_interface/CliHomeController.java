package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.bean.HomeBean;
import it.ispw.daniele.backpacker.controller.search.SearchController;
import it.ispw.daniele.backpacker.exceptions.AddressNotFoundException;
import it.ispw.daniele.backpacker.exceptions.CityNotFoundException;
import it.ispw.daniele.backpacker.exceptions.MonumentNotFoundException;
import it.ispw.daniele.backpacker.view.utils_view.InterfaceController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.*;

public class CliHomeController extends InterfaceController {

    public void init(Scanner scanner) throws IOException, AddressNotFoundException, CityNotFoundException, MonumentNotFoundException {

        System.out.print("\033[H\033[2J");
        System.out.println(BOLD + "SEARCH PAGE\n" + RESET);

        System.out.println("Country:");
        String country = scanner.nextLine();

        System.out.println("City:");
        String city = scanner.nextLine();

        System.out.println("Address:");
        String address = scanner.nextLine();

        System.out.println("Restaurant: [Y or N]");
        String restaurant = scanner.nextLine();

        System.out.println("Range:");
        String range = scanner.nextLine();

        HomeBean homeBean = this.setHomeBean(country, city, address, restaurant, range);

        try {
            if (country.equals("") || city.equals("") || address.equals("")) {
                throw new FileNotFoundException("ERROR");
            }

            SearchController sc = new SearchController();
            sc.checkInput(homeBean);

            CliUserGraphicChange ugc = CliUserGraphicChange.getInstance();
            ugc.switchToResult(homeBean);

        } catch (CityNotFoundException | AddressNotFoundException | MonumentNotFoundException exception) {
            System.out.println(RED + exception.getMessage() + RESET + "\n");
        }
    }
}
