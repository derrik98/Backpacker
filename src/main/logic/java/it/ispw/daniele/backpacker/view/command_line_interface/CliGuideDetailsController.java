package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.bean.TouristGuideBean;
import it.ispw.daniele.backpacker.dao.tourist_guide_dao.TouristGuideDaoFactory;
import it.ispw.daniele.backpacker.dao.tourist_guide_dao.TouristGuideDaoL;
import it.ispw.daniele.backpacker.entity.TouristGuide;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.SessionUser;
import it.ispw.daniele.backpacker.view.utils_view.CliController;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.BOLD;
import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RESET;

public class CliGuideDetailsController extends CliController {

    private TouristGuideBean getSearchUser(String caller) {
        TouristGuideDaoFactory gdf = new TouristGuideDaoL();
        List<TouristGuide> l = null;
        try {
            l = gdf.getSearchUser(caller);
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
        return this.convert(Objects.requireNonNull(l).get(0));
    }

    public void init(Scanner scanner) {

        TouristGuideBean guide = this.getSearchUser(SessionUser.getInstance().getSession().getUsername());

        do {
            System.out.print("\033[H\033[2J");
            System.out.println(BOLD + "PROFILE PAGE\n" + RESET);
            System.out.println("Username: " + guide.getUsername());
            System.out.println("Name: " + guide.getName());
            System.out.println("Email: " + guide.getEmail());
            System.out.println("Surname: " + guide.getSurname());
            System.out.println("Vat number: " + guide.getIdentificationCode());
            System.out.println("\n");

            this.createCliTable(guide.getUsername());

            System.out.println("Go Back [press 'b']: ");

            if (scanner.nextLine().equals("b")) {
                return;
            }
            else {
                System.out.println("Command not found");
            }
        }while (true);
    }
}
