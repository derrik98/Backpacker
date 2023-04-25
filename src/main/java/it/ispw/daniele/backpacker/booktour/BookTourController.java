package it.ispw.daniele.backpacker.booktour;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.dao.ItineraryDao.ItineraryDao;
import it.ispw.daniele.backpacker.entity.Itinerary;
import it.ispw.daniele.backpacker.utils.Controller;
import java.util.Collections;

import java.util.List;

public class BookTourController extends Controller {

    public List<ItineraryBean> getItinerary(String input, String type){
        ItineraryDao id = new ItineraryDao();
        List<Itinerary> itinerary;
        switch (type) {
            case "city" -> itinerary = id.getItinerary(input);
            case "user" -> itinerary = id.getBookedItineraries(input);
            default -> itinerary = Collections.emptyList();
        }
        if(itinerary != null){
            return this.convert(itinerary);
        }
        else{
            return Collections.emptyList();
        }
    }

    public void addParticipation(GeneralUserBean user, ItineraryBean itinerary) {
        AddParticipationController apc = new AddParticipationController();
        apc.addParticipation(user, itinerary);
    }

    public void removeParticipation(GeneralUserBean user, ItineraryBean itinerary) {
        AddParticipationController apc = new AddParticipationController();
        apc.removeParticipation(user, itinerary);
    }

    public boolean isParticipating(GeneralUserBean user, ItineraryBean itinerary) {
        AddParticipationController apc = new AddParticipationController();
        return apc.isParticipating(user, itinerary);
    }

}
