package it.ispw.daniele.backpacker.view.utils_view;

import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.bean.SearchBean;
import it.ispw.daniele.backpacker.bean.TouristGuideBean;
import it.ispw.daniele.backpacker.bean.UserBean;
import it.ispw.daniele.backpacker.entity.Itinerary;
import it.ispw.daniele.backpacker.entity.TouristGuide;
import it.ispw.daniele.backpacker.entity.User;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.FileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public abstract class Controller {

    protected UserBean convert(User l) { //creates a bean from an entity
        UserBean ub = new UserBean();
        ub.setUsername(l.getUsername());
        ub.setName(l.getName());
        ub.setSurname(l.getSurname());
        ub.setProfilePicture(l.getProfilePicture());
        ub.setEmail(l.getEmail());

        return ub;
    }

    protected TouristGuideBean convert(TouristGuide l) { //creates a bean from an entity
        TouristGuideBean tgb = new TouristGuideBean();
        tgb.setUsername(l.getUsername());
        tgb.setName(l.getName());
        tgb.setSurname(l.getSurname());
        tgb.setProfilePicture(l.getProfilePicture());
        tgb.setEmail(l.getEmail());
        tgb.setIdentificationCode(l.getVatNum());

        return tgb;
    }

    protected List<ItineraryBean> convert(List<Itinerary> itinerary) { //creates a bean from an entity
        List<ItineraryBean> lb = new ArrayList<>();
        for (Itinerary it : itinerary) {
            ItineraryBean ib = this.convert(it);
            lb.add(ib);
        }

        return lb;
    }

    protected ItineraryBean convert(Itinerary itinerary) {
        ItineraryBean ib = new ItineraryBean();

        ib.setItineraryId(itinerary.getId());
        ib.setGuideId(itinerary.getGuideId());
        ib.setLocation(itinerary.getLocation());
        ib.setDate(itinerary.getDate());
        ib.setTime(itinerary.getTime());
        ib.setParticipants(itinerary.getParticipants());
        ib.setPrice(itinerary.getPrice());
        ib.setSteps(itinerary.getSteps());

        return ib;
    }

    protected UserBean setUserBean(String username, String name, String surname, String email, String password, String newFileName){ //create a bean from single inputs
        UserBean ub = new UserBean();
        ub.setUsername(username);
        ub.setName(name);
        ub.setSurname(surname);
        ub.setEmail(email);
        ub.setPassword(password);
        ub.setProfilePicture(newFileName);
        return ub;
    }
    protected TouristGuideBean setTouristGuideBean(String username, String name, String surname, String email, String password, String newFileName, String vatNumber){ //create a bean from single inputs
        TouristGuideBean tgb = new TouristGuideBean();
        tgb.setUsername(username);
        tgb.setName(name);
        tgb.setSurname(surname);
        tgb.setEmail(email);
        tgb.setPassword(password);
        tgb.setProfilePicture(newFileName);
        tgb.setIdentificationCode(vatNumber);
        return tgb;
    }

    protected ItineraryBean setItineraryBean(String guideId, String location, String date, String time, int participants, int price, String steps){ //create a bean from single inputs
        ItineraryBean ib = new ItineraryBean();
        ib.setGuideId(guideId);
        ib.setDate(date);
        ib.setLocation(location);
        ib.setTime(time);
        ib.setParticipants(participants);
        ib.setPrice(price);
        ib.setSteps(steps);
        return ib;
    }

    protected SearchBean setHomeBean(String country, String city, String address, String restaurant, String range){ //create a bean from single inputs
        SearchBean hb = new SearchBean();
        hb.setCountry(country);
        hb.setCity(city);
        hb.setAddress(address);
        hb.setRestaurant(restaurant);
        hb.setRange(range);
        return hb;
    }

    protected void setImage(File imageFile, String fileName, String newFileName) throws GenericException { //set image when I create a user
        if(imageFile != null){
            String path = FileManager.PROFILE;

            File file = new File(path, fileName);
            File newFile = new File(path, newFileName);
            try(InputStream inputStream = new FileInputStream(imageFile)){
                Files.copy(inputStream, file.toPath());
            }catch (Exception e){
                throw new GenericException("Warning image");
            }
            if(!file.renameTo(newFile)){
                throw new GenericException("Unable to rename");
            }
        }
    }

}
