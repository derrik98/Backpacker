package it.ispw.daniele.backpacker.controller.add_itinerary;

import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.dao.ItineraryDao.ItineraryDao;
import it.ispw.daniele.backpacker.dao.ItineraryDao.ItineraryDaoFactory;
import it.ispw.daniele.backpacker.dao.ItineraryDao.ItineraryDaoL;
import it.ispw.daniele.backpacker.exceptions.DateException;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItineraryController {

    private ItineraryDaoFactory itineraryDao = null;

    public AddItineraryController(String view) {

        switch (view) {
            case "gui" -> this.itineraryDao = new ItineraryDao();
            case "cli" -> this.itineraryDao = new ItineraryDaoL();
        }

    }

    public int getItineraryId(ItineraryBean itineraryBean) throws SQLException, FileNotFoundException {
        //ItineraryDao itineraryDao = new ItineraryDao();
        ///MODIFICARE CODICE SQL
        return this.itineraryDao.getItineraryId(itineraryBean.getGuideId(), itineraryBean.getLocation(), itineraryBean.getDate(), itineraryBean.getTime(), itineraryBean.getParticipants(), itineraryBean.getPrice(), itineraryBean.getSteps());
    }

    public boolean addItinerary(ItineraryBean itineraryBean) throws DateException {
        //ItineraryDao itineraryDao = new ItineraryDao();
        Date date;
        Date currentDate = new Date();

        if(itineraryBean.getGuideId().equals("") || itineraryBean.getLocation().equals("")) {
            return false;
        }

        if (itineraryBean.getDate() != null) {
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(itineraryBean.getDate());
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }

        if(date.before(currentDate)){
            DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
            throw new DateException(outputFormatter.format(date) + " is before current date");
        }

        return this.itineraryDao.addItinerary(itineraryBean.getGuideId(), itineraryBean.getLocation(), date, itineraryBean.getTime(), itineraryBean.getParticipants(), itineraryBean.getPrice(), itineraryBean.getSteps());

    }
}
