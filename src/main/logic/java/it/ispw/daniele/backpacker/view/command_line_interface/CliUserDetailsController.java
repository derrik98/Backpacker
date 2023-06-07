package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.bean.UserBean;
import it.ispw.daniele.backpacker.booktour.BookTourController;
import it.ispw.daniele.backpacker.booktour.SaveTour;
import it.ispw.daniele.backpacker.dao.user_dao.UserDaoFactory;
import it.ispw.daniele.backpacker.dao.user_dao.UserDaoL;
import it.ispw.daniele.backpacker.entity.User;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.Controller;
import it.ispw.daniele.backpacker.utils.SessionUser;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.BOLD;
import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RESET;

public class CliUserDetailsController extends Controller {

    private UserBean getSearchUser(String caller) {

        UserDaoFactory ud = new UserDaoL();
        List<User> l = null;
        try {
            l = ud.getSearchUser(caller);
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
        return this.convert(Objects.requireNonNull(l).get(0));
    }

    public void init() {

        Scanner scanner = new Scanner(System.in);
        UserBean users = this.getSearchUser(SessionUser.getInstance().getSession().getUsername());

        do {

            System.out.print("\033[H\033[2J");
            System.out.println(BOLD + "PROFILE PAGE\n" + RESET);
            System.out.println("Username: " + users.getUsername());
            System.out.println("Email: " + users.getEmail());
            System.out.println("Name: " + users.getName());
            System.out.println("Surname: " + users.getSurname());
            System.out.println("\n");

            List<ItineraryBean> booked = this.getBookedIt(users);

            List<ItineraryBean> saved = this.getIt(users);

            if(booked == null){
                System.out.println("Booked itineraries: ");
                System.out.println("EMPTY_DATABASE\n");
            } else {
                this.displayBIt(booked);
            }

            if(saved == null){
                System.out.println("Saved itineraries: ");
                System.out.println("EMPTY_DATABASE\n");
            } else {
                this.displayIt(saved);
            }

            System.out.println("\nGo Back [b]: ");

            if (scanner.nextLine().equals("b")) {
                return;
            } else {
                System.out.println("Command not found");
            }

        } while (scanner.hasNext());
    }

    private void displayIt(List<ItineraryBean> saved) {
        for (int indexS = 0; indexS < saved.size(); indexS++) {
            System.out.println("Saved itineraries: ");
            System.out.print("ID [" + saved.get(indexS).getItineraryId() + "] " + saved.get(indexS).getSteps() + "\n");
        }
    }

    private void displayBIt(List<ItineraryBean> booked) {
        for (int indexB = 0; indexB < booked.size(); indexB++) {
            System.out.println("Booked itineraries: ");
            System.out.print("ID [" + booked.get(indexB).getItineraryId() + "] " + booked.get(indexB).getSteps() + "\n");
        }
    }

    private List<ItineraryBean> getBookedIt(UserBean users) {

        BookTourController btc = new BookTourController("cli");
        List<ItineraryBean> booked = null;
        try {
            booked = btc.getItinerary(users.getUsername(), "user");
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
        return booked;
    }

    private List<ItineraryBean> getIt(UserBean users) {

        SaveTour st = new SaveTour("cli");
        List<ItineraryBean> saved = null;
        try {
            saved = st.getItinerary(users.getUsername());
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
        return saved;
    }
}
