package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.bean.SearchBean;
import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.booktour.BookTourController;
import it.ispw.daniele.backpacker.booktour.SaveTour;
import it.ispw.daniele.backpacker.controller.search.SearchController;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.exceptions.MonumentNotFoundException;
import it.ispw.daniele.backpacker.utils.SessionUser;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.*;

public class CliResultController {

    List<ItineraryBean> it;

    private static final String ITINERARY_ID = "Itinerary id";

    public void init(Scanner scanner) throws MonumentNotFoundException, GenericException {
        System.out.print("\033[H\033[2J");

        SearchBean searchBean = SessionUser.getInstance().getSearchSession();

        System.out.println(BOLD + "RESULT PAGE\n" + RESET);
        System.out.println("Country: " + searchBean.getCountry() + ", City: " + searchBean.getCity() + ", Address: " + searchBean.getAddress() + ", Restaurant: " + searchBean.isRestaurant() + ", Range: " + searchBean.getRange() + "\n");

        BookTourController btc = new BookTourController("cli");

        it = btc.getItinerary(searchBean.getCity(), "city");
        int bookedSize = 0;

        if (it == null || it.isEmpty()) {
            System.out.println("Suggested Itinerary: " + RED + "EMPTY_DATABASE " + RESET + "\n");
        } else {
            System.out.println("Suggested Itinerary: \n");
            bookedSize = it.size();
            createTable(it, 0);
        }

        SearchController sc = new SearchController();
        List<ItineraryBean> iti;

        iti = sc.createItinerary(searchBean);

        if (iti == null) {
            System.out.println("Self Itinerary: " + RED + "EMPTY_DATABASE " + RESET + "\n");
        } else {
            System.out.print("Self Itinerary: \n");
            createTable(iti, bookedSize);
        }
        assert it != null;
        List<ItineraryBean> mergeItinerary = new ArrayList<>(it);
        assert iti != null;
        mergeItinerary.addAll(iti);
        createCommand(mergeItinerary, bookedSize - 1, scanner);
    }

    private void createCommand(List<ItineraryBean> itineraryBeanList, int bSize, Scanner scanner){

        do {
            System.out.println("Commands : VIEW ON MAP[0] - SAVE[1] - BUY[2] (Only for suggester itinerary) - QUIT[3]");

            switch (scanner.nextLine()) {
                case "0" -> this.viewOnMap(scanner, itineraryBeanList);
                case "1" -> this.save(scanner, itineraryBeanList);
                case "2" -> this.buy(scanner, itineraryBeanList, bSize);
                case "3" -> {
                    System.out.println("QUIT");
                    return;
                }
                default -> System.out.println(RED + "Command not found\n" + RESET);
            }
        } while (true);
    }

    public void createTable(List<ItineraryBean> itineraryBeanList, int tableSize) {

        int j;
        for (j = 0; j < itineraryBeanList.size(); j++) {
            String[] steps = itineraryBeanList.get(j).getSteps().split("/");
            ArrayList<String> als = new ArrayList<>();
            for (int i = 0; i < steps.length; i++) {
                als.add(i, steps[i]);
            }

            StringBuilder line = new StringBuilder();
            line.append("ID [").append(tableSize).append("] ");
            tableSize++;
            for (int indexMonument = 0; indexMonument < als.size(); indexMonument++) {
                line.append(als.get(indexMonument));

            }
            System.out.println(line);
            System.out.print("\n");

        }
    }

    private void viewOnMap(Scanner scanner, List<ItineraryBean> itineraryBeanList) {
        System.out.println(ITINERARY_ID);
        int input = Integer.parseInt(scanner.nextLine());

        String[] steps = itineraryBeanList.get(input).getSteps().split("/");

        ArrayList<String> als = new ArrayList<>();
        for (int i = 0; i < steps.length; i++) {
            als.add(i, steps[i]);
        }

        StringBuilder url = new StringBuilder("https://google.it/maps/dir");

        for (int indexMonument = 0; indexMonument < als.size(); indexMonument++) {
            url.append("/").append(als.get(indexMonument));
        }

        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url.toString().replace(" ", "+")));
            } catch (IOException | URISyntaxException e) {
                System.out.println(RED + "Unable to view on map\n" + RESET);
            }
        } else {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
            } catch (IOException e) {
                System.out.println(RED + "Unable to view on map\n" + RESET);
            }
        }
    }

    private void save(Scanner scanner, List<ItineraryBean> itineraryBeanList) {
        SaveTour st = new SaveTour("cli");
        System.out.println(ITINERARY_ID);
        int input = Integer.parseInt(scanner.nextLine());

        if (input <= itineraryBeanList.size() && input >= 0) {
            try {
                st.saveTour(SessionUser.getInstance().getSession(), itineraryBeanList.get(input));
            } catch (GenericException e) {
                System.out.println(RED + e.getMessage() + "\n" + RESET);
            }
            System.out.println(GREEN + "Itinerary added successfully" + RESET);
        } else {
            System.out.println(RED + "Incorrect id" + RESET);
        }
    }

    private void buy(Scanner scanner, List<ItineraryBean> itineraryBeanList, int bSize){
        System.out.println(ITINERARY_ID);
        int input = Integer.parseInt(scanner.nextLine());
        if (input <= bSize && input >= bSize) {
            CliItineraryDetailsController idc = new CliItineraryDetailsController();
            idc.init(itineraryBeanList.get(input));
        } else {
            System.out.println(RED + "Incorrect id" + RESET);
        }
    }
}
