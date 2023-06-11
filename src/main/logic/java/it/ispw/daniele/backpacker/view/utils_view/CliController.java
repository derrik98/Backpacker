package it.ispw.daniele.backpacker.view.utils_view;

import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.controller.book_tour.BookTourController;
import it.ispw.daniele.backpacker.controller.book_tour.SaveItinerary;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.SessionUser;

import java.util.List;
import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RED;
import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RESET;

public class CliController extends Controller {

    protected void createCliTable(Scanner scanner, String user) throws GenericException {

        do {

            List<ItineraryBean> booked = this.getBookedIt(user);

            List<ItineraryBean> saved = this.getIt(user);

            if (booked == null || booked.isEmpty()) {
                System.out.println("Booked itineraries: ");
                System.out.println("EMPTY_DATABASE\n");
            } else {
                this.cliDisplayBIt(booked);
            }

            if (saved == null || saved.isEmpty()) {
                System.out.println("Saved itineraries: ");
                System.out.println("EMPTY_DATABASE\n");
            } else {
                this.cliDisplayIt(saved);
            }

            System.out.println("Commands : REMOVE BOOKED[0] - REMOVE SAVED[1] - GO BACK[2]");

            int input = Integer.parseInt(scanner.nextLine());
            switch (input) {
                case 0 -> {
                    assert booked != null;
                    this.removeB(input, booked);
                }
                case 1 -> {
                    assert saved != null;
                    this.removeS(input, saved);
                }
                case 2 -> {
                    return;
                }
                default -> System.out.println(RED + "Command not found\n" + RESET);
            }

        }while (true);
    }

    private void removeS(int input, List<ItineraryBean> saved) throws GenericException {
        SaveItinerary st = new SaveItinerary("gui");
        st.removeItinerary(SessionUser.getInstance().getSession(), saved.get(input));
    }

    private void removeB(int input, List<ItineraryBean> booked) throws GenericException {

        BookTourController btc = new BookTourController("cli");
        btc.removeParticipation(SessionUser.getInstance().getSession(), booked.get(input));
    }

    private List<ItineraryBean> getBookedIt(String user) {

        BookTourController btc = new BookTourController("cli");
        List<ItineraryBean> booked = null;
        try {
            booked = this.convert(btc.getItinerary(user, "user"));
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
        return booked;
    }

    private List<ItineraryBean> getIt(String user) {

        SaveItinerary st = new SaveItinerary("cli");
        List<ItineraryBean> saved = null;
        try {
            saved = this.convert(st.getItinerary(user));
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
        return saved;
    }

    protected void cliDisplayIt(List<ItineraryBean> saved) {
        for (int indexS = 0; indexS < saved.size(); indexS++) {
            System.out.println("Saved itineraries: ");
            System.out.print("ID [" + saved.get(indexS).getItineraryId() + "] " + saved.get(indexS).getSteps() + "\n");
        }
    }

    protected void cliDisplayBIt(List<ItineraryBean> booked) {
        for (int indexB = 0; indexB < booked.size(); indexB++) {
            System.out.println("Booked itineraries: ");
            System.out.print("ID [" + booked.get(indexB).getItineraryId() + "] " + booked.get(indexB).getSteps() + "\n");
        }
    }

}
