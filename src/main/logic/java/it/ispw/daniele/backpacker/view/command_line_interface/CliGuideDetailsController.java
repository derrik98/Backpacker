package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.bean.TouristGuideBean;
import it.ispw.daniele.backpacker.booktour.BookTourController;
import it.ispw.daniele.backpacker.booktour.SaveItinerary;
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
        TouristGuideBean guide = this.getSearchUser(gub.getUsername());
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print("\033[H\033[2J");
            System.out.println(BOLD + "PROFILE PAGE\n" + RESET);
            System.out.println("Username: " + guide.getUsername());
            System.out.println("Name: " + guide.getName());
            System.out.println("Email: " + guide.getEmail());
            System.out.println("Surname: " + guide.getSurname());
            System.out.println("Vat number: " + guide.getIdentificationCode());
            System.out.println("\n");

            List<ItineraryBean> booked = this.getBookedIt(guide);

            List<ItineraryBean> saved = this.getIt(guide);

            if(booked == null){
                System.out.println("Booked itineraries: ");
                System.out.println("EMPTY_DATABASE\n");
            } else {
                this.cliDisplayBIt(booked);
            }

            if(saved == null){
                System.out.println("Saved itineraries: ");
                System.out.println("EMPTY_DATABASE\n");
            } else {
                this.cliDisplayIt(saved);
            }

            System.out.println("Go Back [press 'b']: ");

            if (scanner.nextLine().equals("b")) {
                return;
            }
            else {
                System.out.println("Command not found");
            }
        }while (scanner.hasNext());
    }

    private List<ItineraryBean> getBookedIt(TouristGuideBean guide) {

        BookTourController btc = new BookTourController("cli");
        List<ItineraryBean> booked = null;
        try {
            booked = btc.getItinerary(guide.getUsername(), "user");
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
        return booked;
    }

    private List<ItineraryBean> getIt(TouristGuideBean guide) {

        SaveItinerary st = new SaveItinerary("cli");
        List<ItineraryBean> saved = null;
        try {
            saved = st.getItinerary(guide.getUsername());
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
        return saved;
    }
}
