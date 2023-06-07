package it.ispw.daniele.backpacker.view.fxml_view;

import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.exceptions.MonumentNotFoundException;

import java.io.IOException;

public interface GuiAction {

    void action() throws IOException, MonumentNotFoundException, GenericException;

}
