package it.ispw.daniele.backpacker.view.utils_view;

import it.ispw.daniele.backpacker.bean.SearchBean;
import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.bean.TouristGuideBean;
import it.ispw.daniele.backpacker.bean.UserBean;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.FileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

public abstract class InterfaceController {

    protected UserBean setUserBean(String username, String name, String surname, String email, String password, String newFileName){
        UserBean ub = new UserBean();
        ub.setUsername(username);
        ub.setName(name);
        ub.setSurname(surname);
        ub.setEmail(email);
        ub.setPassword(password);
        ub.setProfilePicture(newFileName);
        return ub;
    }
    protected TouristGuideBean setTouristGuideBean(String username, String name, String surname, String email, String password, String newFileName, String vatNumber){
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

    protected ItineraryBean setItineraryBean(String guideId, String location, String date, String time, int participants, int price, String steps){
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

    protected SearchBean setHomeBean(String country, String city, String address, String restaurant, String range){
        SearchBean hb = new SearchBean();
        hb.setCountry(country);
        hb.setCity(city);
        hb.setAddress(address);
        hb.setRestaurant(restaurant);
        hb.setRange(range);
        return hb;
    }

    protected void setImage(File imageFile, String fileName, String newFileName) throws GenericException {
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
