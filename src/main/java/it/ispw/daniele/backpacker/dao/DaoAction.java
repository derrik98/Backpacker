package it.ispw.daniele.backpacker.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public interface DaoAction<G> {
    G act() throws SQLException, ClassNotFoundException, IOException;
}
