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

public class SaveTour extends Controller {

    private ItineraryDaoFactory id = null;

    public SaveTour(String view) {

        if (view.equals("gui")) {
            this.id = new ItineraryDao();
        } else if (view.equals("cli")) {
            this.id = new ItineraryDaoL();
        }
    }

    public void saveTour(GeneralUserBean user, ItineraryBean itinerary) {
        this.id.saveTour(user.getUsername(), itinerary.getSteps());
    }

    public void removeTour(GeneralUserBean user, ItineraryBean itinerary){
        this.id.removeTour(user.getUsername(), itinerary.getSteps());
    }

    public List<ItineraryBean> getItinerary(String input){

        List<Itinerary> itinerary;

        itinerary = this.id.getSavedItinerary(input);

        if(itinerary != null){
            return this.convert(itinerary);
        }
        else{
            return Collections.emptyList();
        }
    }

}
