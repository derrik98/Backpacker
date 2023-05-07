package it.ispw.daniele.backpacker.booktour;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.dao.itinerary_dao.ItineraryDao;
import it.ispw.daniele.backpacker.dao.itinerary_dao.ItineraryDaoFactory;
import it.ispw.daniele.backpacker.dao.itinerary_dao.ItineraryDaoL;
import it.ispw.daniele.backpacker.entity.Itinerary;
import it.ispw.daniele.backpacker.utils.Controller;

import java.util.Collections;
import java.util.List;

public class BookTourController extends Controller {

    private final String view;
    private ItineraryDaoFactory id = null;

    public BookTourController(String view) {

        this.view = view;

        if (this.view.equals("gui")) {
            this.id = new ItineraryDao();
        } else if (view.equals("cli")) {
            this.id = new ItineraryDaoL();
        }
    }

    public List<ItineraryBean> getItinerary(String input, String type) {

        List<Itinerary> itinerary;
        switch (type) {
            case "city" -> itinerary = this.id.getItinerary(input);
            case "user" -> itinerary = this.id.getBookedItineraries(input);
            default -> itinerary = Collections.emptyList();
        }

        if (itinerary != null) {
            return this.convert(itinerary);
        } else {
            return Collections.emptyList();
        }
    }

    public void addParticipation(GeneralUserBean user, ItineraryBean itinerary) {
        AddParticipationController apc = new AddParticipationController(this.view);
        apc.addParticipation(user, itinerary);
    }

    public void removeParticipation(GeneralUserBean user, ItineraryBean itinerary) {
        AddParticipationController apc = new AddParticipationController(this.view);
        apc.removeParticipation(user, itinerary);
    }

    public boolean isParticipating(GeneralUserBean user, ItineraryBean itinerary) {
        AddParticipationController apc = new AddParticipationController(this.view);
        return apc.isParticipating(user, itinerary);
    }

}
