package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.exceptions.AddressNotFoundException;
import it.ispw.daniele.backpacker.exceptions.CityNotFoundException;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.exceptions.MonumentNotFoundException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface CliGuiAction {
    public void action() throws IOException, AddressNotFoundException, CityNotFoundException, MonumentNotFoundException, NoSuchAlgorithmException, GenericException;
}
