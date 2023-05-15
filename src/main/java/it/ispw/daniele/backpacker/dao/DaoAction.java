package it.ispw.daniele.backpacker.dao;

import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.exceptions.LoginFailException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;

public interface DaoAction<G> {
    G act() throws SQLException, GenericException, ClassNotFoundException, IOException;
}
