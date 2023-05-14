package it.ispw.daniele.backpacker.booktour;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.dao.itinerary_dao.ItineraryDao;
import it.ispw.daniele.backpacker.dao.itinerary_dao.ItineraryDaoFactory;
import it.ispw.daniele.backpacker.dao.itinerary_dao.ItineraryDaoL;
import it.ispw.daniele.backpacker.exceptions.GenericException;

public class AddParticipationController {

    private ItineraryDaoFactory id = null;

    public AddParticipationController(String view) {

        if (view.equals("gui")) {
            this.id = new ItineraryDao();
        } else if (view.equals("cli")) {
            this.id = new ItineraryDaoL();
        }
    }

    public void addParticipation(GeneralUserBean user, ItineraryBean itinerary) throws GenericException {
        this.id.addParticipation(user.getUsername(), itinerary.getItineraryId());
    }

    public void removeParticipation(GeneralUserBean user, ItineraryBean itinerary) throws GenericException {
        this.id.removeParticipation(user.getUsername(), itinerary.getItineraryId());
    }

    public boolean isParticipating(GeneralUserBean user, ItineraryBean itinerary) throws GenericException {
        return this.id.isParticipating(user.getUsername(), itinerary.getItineraryId());
    }
}

