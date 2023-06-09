package it.ispw.daniele.backpacker.view.command_line_interface;

import it.ispw.daniele.backpacker.bean.UserBean;
import it.ispw.daniele.backpacker.dao.user_dao.UserDaoFactory;
import it.ispw.daniele.backpacker.dao.user_dao.UserDaoL;
import it.ispw.daniele.backpacker.entity.User;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.utils.SessionUser;
import it.ispw.daniele.backpacker.view.utils_view.CliController;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static it.ispw.daniele.backpacker.view.command_line_interface.CLI.*;

public class CliUserDetailsController extends CliController {

    private UserBean getSearchUser(String caller) {

        UserDaoFactory ud = new UserDaoL();
        List<User> l = null;
        try {
            l = ud.getSearchUser(caller);
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
        return this.convert(Objects.requireNonNull(l).get(0));
    }

    public void init(Scanner scanner) {

        UserBean users = this.getSearchUser(SessionUser.getInstance().getSession().getUsername());

        do {

            System.out.print("\033[H\033[2J");
            System.out.println(BOLD + "PROFILE PAGE\n" + RESET);
            System.out.println("Username: " + users.getUsername());
            System.out.println("Email: " + users.getEmail());
            System.out.println("Name: " + users.getName());
            System.out.println("Surname: " + users.getSurname());
            System.out.println("\n");

            try {
                this.createCliTable(scanner, users.getUsername());
                return;
            } catch (GenericException e) {
                System.out.println(RED + "Command not found\n" + RESET);
            }

        } while (true);
    }
}
