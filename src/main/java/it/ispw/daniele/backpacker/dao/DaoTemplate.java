package it.ispw.daniele.backpacker.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DaoTemplate {

    protected static final Logger logger = Logger.getLogger("Dao");

    public final <G> G execute(DaoAction<G> daoAction){
        try {
            return daoAction.act();
        }catch (SQLException | ClassNotFoundException | FileNotFoundException exception){
            logger.log(Level.WARNING, exception.toString(), exception.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
