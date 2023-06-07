package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.bean.TouristGuideBean;
import it.ispw.daniele.backpacker.dao.tourist_guide_dao.TouristGuideDao;
import it.ispw.daniele.backpacker.entity.TouristGuide;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.Controller;
import it.ispw.daniele.backpacker.utils.SessionUser;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.BOLD;
import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RESET;

public class CliGuideDetailsController extends Controller {

    private TouristGuideBean getSearchUser(String caller) {
        TouristGuideDao ud = new TouristGuideDao();
        List<TouristGuide> l = null;
        try {
            l = ud.getSearchUser(caller);
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
        return this.convert(Objects.requireNonNull(l).get(0));
    }

    public void init() {

        GeneralUserBean gub = SessionUser.getInstance().getSession();
        TouristGuideBean users = this.getSearchUser(gub.getUsername());
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print("\033[H\033[2J");
            System.out.println(BOLD + "PROFILE PAGE\n" + RESET);
            System.out.println("Username: " + users.getUsername());
            System.out.println("Name: " + users.getName());
            System.out.println("Email: " + users.getEmail());
            System.out.println("Surname: " + users.getSurname());
            System.out.println("Vat number: " + users.getIdentificationCode());
            System.out.println("\n");
            System.out.println("Go Back [press 'b']: ");

            if (scanner.nextLine().equals("b")) {
                return;
            }
            else {
                System.out.println("Incorrect command!");
            }
        }while (true);
    }
}