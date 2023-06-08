package it.ispw.daniele.backpacker.controller.add_itinerary;

import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.dao.itinerary_dao.ItineraryDao;
import it.ispw.daniele.backpacker.dao.itinerary_dao.ItineraryDaoFactory;
import it.ispw.daniele.backpacker.dao.itinerary_dao.ItineraryDaoL;
import it.ispw.daniele.backpacker.exceptions.DateException;
import it.ispw.daniele.backpacker.exceptions.GenericException;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItineraryController {

    private ItineraryDaoFactory itineraryDao = null;

    public AddItineraryController(String view) {

        if (view.equals("gui")) {
            this.itineraryDao = new ItineraryDao();
        } else if (view.equals("cli")) {
            this.itineraryDao = new ItineraryDaoL();
        }

    }

    public int getItineraryId(ItineraryBean itineraryBean) throws SQLException, FileNotFoundException, GenericException, ClassNotFoundException {
        return this.itineraryDao.getItineraryId(itineraryBean);
    }

    public boolean addItinerary(ItineraryBean itineraryBean) throws DateException, GenericException {
        Date date;
        Date currentDate = new Date();


        if(itineraryBean.getGuideId().equals("") || itineraryBean.getLocation().equals("")) {
            throw new GenericException("Data missing");
        }

        if (itineraryBean.getDate() != null) {
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(itineraryBean.getDate());
                System.out.println(date);
                System.out.println(itineraryBean.getDate());
            } catch (ParseException e) {
                throw new GenericException("Data missing");
            }
        } else {
            throw new GenericException("Data missing");
        }

        if(date.before(currentDate)){
            DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
            throw new DateException(outputFormatter.format(date) + " is before current date");
        }

        return this.itineraryDao.addItinerary(itineraryBean, date);

    }
}
