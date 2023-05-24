package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.exceptions.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public interface CliAction {
    void action() throws IOException, AddressNotFoundException, CityNotFoundException,
            MonumentNotFoundException, NoSuchAlgorithmException, GenericException, SQLException, EmptyFieldException;
}
