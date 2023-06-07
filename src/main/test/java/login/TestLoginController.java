package login;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.controller.login.LoginController;
import it.ispw.daniele.backpacker.exceptions.EmptyFieldException;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLoginController {

    @Test
    public void testUsernameEmptyLogin() {

        LoginController lc;
        String output;
        String attended;
        String username;
        String password;
        GeneralUserBean gb;

        gb = new GeneralUserBean();
        lc = new LoginController();
        output = "";
        attended = "Username necessary";
        username="";
        password = "password";
        gb.setPassword(password);
        gb.setUsername(username);


        try {
            lc.login(gb, "gui");
        } catch (EmptyFieldException | GenericException e) {
            output = e.getMessage();
        } finally {
            assertEquals(attended, output);
        }
    }
}
