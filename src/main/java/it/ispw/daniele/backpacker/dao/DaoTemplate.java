package it.ispw.daniele.backpacker.dao;

import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.exceptions.LoginFailException;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DaoTemplate {

    protected static final Logger logger = Logger.getLogger("Dao");

    public final <G> G execute(DaoAction<G> daoAction) throws GenericException {
        try {
            return daoAction.act();
        }catch (SQLException | GenericException | ClassNotFoundException exception){
            throw new GenericException("ERRORE");
            //logger.log(Level.WARNING, exception.toString(), exception.getMessage());
        }
        //return null;
    }
}
