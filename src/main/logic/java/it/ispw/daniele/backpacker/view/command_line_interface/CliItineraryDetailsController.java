package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.controller.book_tour.BookTourController;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.SessionUser;

import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.*;

public class CliItineraryDetailsController {

    private String command = "SUBSCRIBE";
    private final BookTourController controller = new BookTourController("cli");
    private ItineraryBean ib = new ItineraryBean();
    private final GeneralUserBean sessionUser = SessionUser.getInstance().getSession();

    public void subscribe() {

        boolean isPart;
        try {
            isPart = controller.isParticipating(this.sessionUser, ib);
            if (isPart) {
                controller.removeParticipation(this.sessionUser, ib);
                System.out.print("Itinerary removed!");
                this.command = "SUBSCRIBE";
            } else {
                controller.addParticipation(this.sessionUser, ib);
                System.out.println("Itinerary added!");
                this.command = "REMOVE";
            }
        } catch (GenericException e) {
            System.out.println(RED + e.getMessage() + "\n" + RESET);
        }

    }

    public void init(Scanner scanner, ItineraryBean itineraryBean) {

        System.out.print("\033[H\033[2J");
        System.out.println(BOLD + "ITINERARY DETAILS PAGE\n" + RESET);

        System.out.println("Itinerary id: " + itineraryBean.getItineraryId());

        System.out.println("Guide id: " + itineraryBean.getGuideId());

        System.out.println("Location: " + itineraryBean.getLocation());

        System.out.println("Date: " + itineraryBean.getDate());

        System.out.println("Time: " + itineraryBean.getTime());

        System.out.println("Participants: " + itineraryBean.getParticipants());

        System.out.println("Price: " + itineraryBean.getPrice());

        System.out.println("Steps: " + itineraryBean.getSteps());

        ib = itineraryBean;

        do {

            System.out.println("\nCOMMANDS [0] " + command + " - [1] UNDO\n");

            System.out.println("Digit command: ");


            if (scanner.nextLine().equals("0")) {
                this.subscribe();
            } else {
                return;
            }

        } while (true);


    }
}
