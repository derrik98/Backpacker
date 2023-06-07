package it.ispw.daniele.backpacker.dao;

import it.ispw.daniele.backpacker.exceptions.GenericException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DaoTemplate {

    protected static final Logger logger = Logger.getLogger("Dao");

    protected JSONObject openFile(String path) throws GenericException {

        JSONParser parser = new JSONParser();
        JSONObject object;

        try {
            object = (JSONObject) parser.parse(new FileReader(path));
        } catch (IOException | ParseException e) {
            throw new GenericException(e.getMessage());
        }
        return object;
    }

    protected void writeOnFile(String path, JSONObject object) throws GenericException {

        try (FileWriter file = new FileWriter(path)) {
            file.write(object.toString());
        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }
    }

    protected final <G> G execute(DaoAction<G> daoAction) throws GenericException {
        try {
            return daoAction.act();
        }catch (SQLException | GenericException | ClassNotFoundException exception){
            logger.log(Level.WARNING, exception.toString(), exception.getMessage());
            throw new GenericException("Remote Database Error");
        } catch (IOException | ParseException e) {
            throw new GenericException("Local Database Error");
        }
    }
}
