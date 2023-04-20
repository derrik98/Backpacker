package it.ispw.daniele.backpacker.dao;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;

public interface DaoAction<G> {
    G act() throws SQLException, ClassNotFoundException, IOException, ParseException;
}
