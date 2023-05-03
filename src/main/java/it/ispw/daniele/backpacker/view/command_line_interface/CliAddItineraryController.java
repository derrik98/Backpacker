package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.bean.ItineraryBean;
import it.ispw.daniele.backpacker.controller.add_itinerary.AddItineraryController;
import it.ispw.daniele.backpacker.utils.SessionUser;
import it.ispw.daniele.backpacker.view.utils_view.InterfaceController;

import java.util.ArrayList;
import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RED;
import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.RESET;

public class CliAddItineraryController extends InterfaceController {

    private String steps = "";
    private GeneralUserBean guideBean = SessionUser.getInstance().getSession();
    private final ArrayList<String> listView = new ArrayList<>();
    private AddItineraryController controller;
    Scanner scanner = new Scanner(System.in);

    public void init() {

        this.controller = new AddItineraryController("cli");

        System.out.println("Digit 'share' to share itinerary");

        for (int i = 1; i < 10; i++) {

            System.out.println("step -> " + i);

            String input = scanner.nextLine();

            if (input.equals("share")) {
                this.share();
                return;
            } else {
                listView.add(input);
            }
        }
        this.guideBean = SessionUser.getInstance().getSession();
    }

    private void share() {

        for (int i = 0; i < this.listView.size(); i++) {
            if (!this.listView.get(i).equals("")) {
                this.steps = this.steps.concat(this.listView.get(i) + "-");
            }
        }

        System.out.println("Itinerary details:\n");

        System.out.println("Location:");
        String location = scanner.nextLine();
        //System.out.flush();

        System.out.println("Date: format[YYYY/MM/DD]");
        String date = scanner.nextLine().replace("/", "-");
        //System.out.flush();

        System.out.println("Time:");
        String time = scanner.nextLine();
        //System.out.flush();

        System.out.println("Participants:");
        String participants = scanner.nextLine();
        //System.out.flush();

        System.out.println("Price:");
        String price = scanner.nextLine();
        //System.out.flush();

        boolean result;

        ItineraryBean itineraryBean = this.setItineraryBean(this.guideBean.getUsername(), location, date, time, Integer.parseInt(participants), Integer.parseInt(price), this.steps);

        try {
            result = controller.addItinerary(itineraryBean);
            if (result) {
                System.out.println("Correct share!\n");
            } else {
                System.out.print(RED + "Error share\n" + RESET);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(RED + "Error share\n" + RESET);
        }
    }
}
