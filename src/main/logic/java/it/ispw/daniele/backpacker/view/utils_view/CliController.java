package it.ispw.daniele.backpacker.view.utils_view;

import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.booktour.BookTourController;
import it.ispw.daniele.backpacker.booktour.SaveItinerary;
import it.ispw.daniele.backpacker.exceptions.GenericException;

import java.util.List;

public class CliController extends Controller {

    protected void createCliTable(String user) {

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
