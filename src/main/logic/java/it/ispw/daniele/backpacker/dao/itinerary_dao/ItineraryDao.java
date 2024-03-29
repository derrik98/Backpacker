package it.ispw.daniele.backpacker.dao.itinerary_dao;

import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.entity.Itinerary;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.DatabaseUserConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ItineraryDao extends ItineraryDaoFactory {

    public List<Itinerary> getItinerary(String city) throws GenericException {
        List<Itinerary> ret = this.execute(() -> {
            Connection conn;
            List<Itinerary> itinerary;
            String sql;

            conn = DatabaseUserConnection.getUserConnection();
            sql = "call backpacker.get_itinerary(?);\r\n";

            try (PreparedStatement stm = conn.prepareStatement(sql)) {

                stm.setString(1, city);

                try (ResultSet rs = stm.executeQuery()) {
                    itinerary = unpackResultSet(rs);
                }
            }
            DatabaseUserConnection.closeUserConnection(conn);
            return itinerary;
        });
        return Objects.requireNonNullElse(ret, Collections.emptyList());
    }

    public Boolean isParticipating(String username, int itineraryId) throws GenericException {
        Boolean ret = this.execute(() -> {
            Connection conn = DatabaseUserConnection.getUserConnection();
            String sql = "call backpacker.is_participating(?, ?);\r\n";
            try (PreparedStatement stm = conn.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setInt(2, itineraryId);
                try (ResultSet rs = stm.executeQuery()) {
                    return (rs.first());
                }
            } finally {
                DatabaseUserConnection.closeUserConnection(conn);
            }
        });
        return Objects.requireNonNullElse(ret, false);
    }

    public int getItineraryId(ItineraryBean itineraryBean) throws SQLException, ClassNotFoundException, GenericException {

        Connection conn;
        String sql;

        conn = DatabaseUserConnection.getUserConnection();
        sql = "call backpacker.get_itinerary_id(?, ?, ?, ?, ?, ?, ?);\r\n";

        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, itineraryBean.getGuideId());
            stm.setString(2, itineraryBean.getLocation());
            stm.setString(3, itineraryBean.getDate());
            stm.setString(4, itineraryBean.getTime());
            stm.setInt(5, itineraryBean.getParticipants());
            stm.setInt(6, itineraryBean.getPrice());
            stm.setString(7, itineraryBean.getSteps());

            try (ResultSet rs = stm.executeQuery()) {

                if (!rs.first()) { // rs not empty
                    throw new GenericException("Itinerary id error");
                }

                return rs.getInt("id");
            } finally {
                DatabaseUserConnection.closeUserConnection(conn);
            }
        }
    }

    public List<Itinerary> getBookedItineraries(String input) throws GenericException {
        List<Itinerary> ret = this.execute(() -> {
            Connection conn;
            List<Itinerary> itinerary;
            String sql;

            conn = DatabaseUserConnection.getUserConnection();
            sql = "call backpacker.get_booked_itineraries(?);\r\n";

            try (PreparedStatement stm = conn.prepareStatement(sql)) {

                stm.setString(1, input);

                try (ResultSet rs = stm.executeQuery()) {
                    itinerary = unpackResultSet(rs);
                }
            } finally {
                DatabaseUserConnection.closeUserConnection(conn);
            }
            return itinerary;
        });
        return Objects.requireNonNullElse(ret, Collections.emptyList());
    }

    private List<Itinerary> unpackResultSet(ResultSet rs) throws SQLException {
        List<Itinerary> l = new ArrayList<>();

        if (!rs.first()) // rs not empty
            return Collections.emptyList();

        do {
            int id = rs.getInt(ID);
            String guideId = rs.getString(GUIDE_ID);
            String location = rs.getString(LOCATION);
            String date = rs.getString(DATE);
            String time = rs.getString(TIME);
            int participants = rs.getInt(PARTICIPANTS);
            int price = rs.getInt(PRICE);
            String steps = rs.getString(STEPS);

            Itinerary itinerary = new Itinerary(guideId, location, date, time, participants, price, steps);
            itinerary.setId(id);

            l.add(itinerary);
        } while (rs.next());
        return l;
    }

    public List<Itinerary> getSavedItinerary(String input) throws GenericException {
        List<Itinerary> ret = this.execute(() -> {
            Connection conn;
            List<Itinerary> itinerary;
            String sql;

            conn = DatabaseUserConnection.getUserConnection();
            sql = "call backpacker.get_saved_itinerary(?);\r\n";

            try (PreparedStatement stm = conn.prepareStatement(sql)) {

                stm.setString(1, input);

                try (ResultSet rs = stm.executeQuery()) {
                    itinerary = unpackResult(rs);
                }
            } finally {
                DatabaseUserConnection.closeUserConnection(conn);
            }
            return itinerary;
        });

        return Objects.requireNonNullElse(ret, Collections.emptyList());
    }

    private List<Itinerary> unpackResult(ResultSet rs) throws SQLException {
        List<Itinerary> l = new ArrayList<>();

        if (!rs.first()) // rs not empty
            return Collections.emptyList();

        do {

            int id = rs.getInt(ID);
            String steps = rs.getString(STEPS);

            Itinerary itinerary = new Itinerary("", "", "", "", 0, 0, steps);
            itinerary.setId(id);

            l.add(itinerary);
        } while (rs.next());
        return l;
    }

}
