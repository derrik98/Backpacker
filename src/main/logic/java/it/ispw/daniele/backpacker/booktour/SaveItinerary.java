package it.ispw.daniele.backpacker.booktour;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.dao.itinerary_dao.ItineraryDao;
import it.ispw.daniele.backpacker.dao.itinerary_dao.ItineraryDaoFactory;
import it.ispw.daniele.backpacker.dao.itinerary_dao.ItineraryDaoL;
import it.ispw.daniele.backpacker.entity.Itinerary;
import it.ispw.daniele.backpacker.exceptions.GenericException;

import java.util.Collections;
import java.util.List;

public class SaveItinerary {

    private ItineraryDaoFactory id = null;

    public SaveItinerary(String view) {

        if (view.equals("gui")) {
            this.id = new ItineraryDao();
        } else if (view.equals("cli")) {
            this.id = new ItineraryDaoL();
        }
    }

    public void saveItinerary(GeneralUserBean user, ItineraryBean itinerary) throws GenericException {
        this.id.saveItinerary(itinerary.getItineraryId(), user.getUsername(), itinerary.getSteps());
    }

    public void removeItinerary(GeneralUserBean user, ItineraryBean itinerary) throws GenericException {
        this.id.removeItinerary(itinerary.getItineraryId(), user.getUsername(), itinerary.getSteps());
    }

    public List<Itinerary> getItinerary(String input) throws GenericException {

        List<Itinerary> itinerary;

        itinerary = this.id.getSavedItinerary(input);

        if(itinerary != null){
            return itinerary;
        }
        else{
            return Collections.emptyList();
        }
    }

}
