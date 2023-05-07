package it.ispw.daniele.backpacker.controller.login;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.bean.TouristGuideBean;
import it.ispw.daniele.backpacker.bean.UserBean;
import it.ispw.daniele.backpacker.dao.general_user_dao.GeneralUserDao;
import it.ispw.daniele.backpacker.dao.general_user_dao.GeneralUserDaoFactory;
import it.ispw.daniele.backpacker.dao.general_user_dao.GeneralUserDaoL;
import it.ispw.daniele.backpacker.dao.tourist_guide_dao.TouristGuideDao;
import it.ispw.daniele.backpacker.dao.tourist_guide_dao.TouristGuideDaoFactory;
import it.ispw.daniele.backpacker.dao.tourist_guide_dao.TouristGuideDaoL;
import it.ispw.daniele.backpacker.dao.user_dao.UserDao;
import it.ispw.daniele.backpacker.dao.user_dao.UserDaoFactory;
import it.ispw.daniele.backpacker.dao.user_dao.UserDaoL;
import it.ispw.daniele.backpacker.entity.GeneralUser;
import it.ispw.daniele.backpacker.exceptions.EmptyFieldException;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.exceptions.LoginFailException;

import java.sql.SQLException;

public class LoginController {

    public GeneralUserBean login(GeneralUserBean userBean, String view) throws EmptyFieldException {

        GeneralUserDaoFactory gudf = null;

        if (userBean.getUsername().equals("")) {
            throw new EmptyFieldException("Username necessary");
        }
        if (userBean.getPassword().equals("")) {
            throw new EmptyFieldException("Password necessary");
        }

        if (view.equals("gui")) {
            gudf = new GeneralUserDao();
        } else if (view.equals("cli")) {
            gudf = new GeneralUserDaoL();
        }

        assert gudf != null;
        GeneralUser result = gudf.findUser(userBean.getUsername(), userBean.getPassword());

        if (result == null) {
            throw new LoginFailException("Incorrect Credentials");
        } else {
            GeneralUserBean gu = new GeneralUserBean();
            gu.setUsername(result.getUsername());
            gu.setPassword(result.getPassword());
            gu.setEmail(result.getEmail());
            gu.setRole(result.getRole());
            return gu;
        }
    }

    public boolean createUser(UserBean ub, String view) throws EmptyFieldException, GenericException, SQLException {

        if (ub.getUsername().equals("") || ub.getName().equals("") || ub.getSurname().equals("") || ub.getEmail().equals("") || ub.getPassword().equals("")) {
            throw new EmptyFieldException("Missing Data");
        }

        UserDaoFactory udf = null;

        if (view.equals("gui")) {
            udf = new UserDao();
        } else if (view.equals("cli")) {
            udf = new UserDaoL();
        }

        assert udf != null;

        if (udf.createUser(ub.getUsername(), ub.getName(), ub.getSurname(), ub.getEmail(), ub.getPassword(), ub.getProfilePicture()).equals(true)) {
            return true;
        }

        else {
            throw new GenericException("User already exist");
        }

    }

    public boolean createTouristGuide(TouristGuideBean tgb, String view) throws EmptyFieldException, GenericException {

        if (tgb.getUsername().equals("") || tgb.getName().equals("") || tgb.getSurname().equals("") || tgb.getEmail().equals("") || tgb.getPassword().equals("") || tgb.getIdentificationCode().equals("")) {
            throw new EmptyFieldException("Missing Data");
        }

        TouristGuideDaoFactory tgdf = null;

        if (view.equals("gui")) {
            tgdf = new TouristGuideDao();
        } else if (view.equals("cli")) {
            tgdf = new TouristGuideDaoL();
        }

        assert tgdf != null;
        if(tgdf.createTouristGuide(tgb.getUsername(), tgb.getName(), tgb.getSurname(), tgb.getEmail(), tgb.getPassword(), tgb.getProfilePicture(), tgb.getIdentificationCode())){
            return true;
        }
        else {
            throw new GenericException("User already exist");
        }

    }
}

