package it.ispw.daniele.backpacker.booktour;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.dao.ItineraryDao.ItineraryDao;
import it.ispw.daniele.backpacker.dao.ItineraryDao.ItineraryDaoFactory;
import it.ispw.daniele.backpacker.dao.ItineraryDao.ItineraryDaoL;
import it.ispw.daniele.backpacker.entity.Itinerary;
import it.ispw.daniele.backpacker.utils.Controller;

import java.util.Collections;
import java.util.List;

public class SaveTour extends Controller {

    private ItineraryDaoFactory id = null;

    public SaveTour(String view) {

        switch (view) {
            case "gui" -> id = new ItineraryDao();
            case "cli" -> id = new ItineraryDaoL();
        }
    }

    public void saveTour(GeneralUserBean user, ItineraryBean itinerary) {
        //ItineraryDao id = new ItineraryDao();
        id.saveTour(user.getUsername(), itinerary.getSteps());
    }

    public void removeTour(GeneralUserBean user, ItineraryBean itinerary){
        //ItineraryDao id = new ItineraryDao();
        id.removeTour(user.getUsername(), itinerary.getSteps());
    }

    public List<ItineraryBean> getItinerary(String input){
        //ItineraryDao id = new ItineraryDao();
        List<Itinerary> itinerary;

        itinerary = id.getSavedItinerary(input);

        if(itinerary != null){
            return this.convert(itinerary);
        }
        else{
            return Collections.emptyList();
        }
    }

}
