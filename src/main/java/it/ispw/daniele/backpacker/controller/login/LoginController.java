package it.ispw.daniele.backpacker.controller.login;

import it.ispw.daniele.backpacker.bean.GeneralUserBean;
import it.ispw.daniele.backpacker.bean.TouristGuideBean;
import it.ispw.daniele.backpacker.bean.UserBean;
import it.ispw.daniele.backpacker.dao.GeneralUserDao.GeneralUserDao;
import it.ispw.daniele.backpacker.dao.GeneralUserDao.GeneralUserDaoFactory;
import it.ispw.daniele.backpacker.dao.GeneralUserDao.GeneralUserDaoL;
import it.ispw.daniele.backpacker.dao.TouristGuideDao.TouristGuideDao;
import it.ispw.daniele.backpacker.dao.TouristGuideDao.TouristGuideDaoFactory;
import it.ispw.daniele.backpacker.dao.TouristGuideDao.TouristGuideDaoL;
import it.ispw.daniele.backpacker.dao.UserDao.UserDao;
import it.ispw.daniele.backpacker.dao.UserDao.UserDaoFactory;
import it.ispw.daniele.backpacker.dao.UserDao.UserDaoL;
import it.ispw.daniele.backpacker.entity.GeneralUser;
import it.ispw.daniele.backpacker.exceptions.EmptyFieldException;
import it.ispw.daniele.backpacker.exceptions.GenericException;
import it.ispw.daniele.backpacker.exceptions.LoginFailException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class LoginController {

    public GeneralUserBean login(GeneralUserBean userBean, String view) throws EmptyFieldException {
        //GeneralUserDao gud = new GeneralUserDao();

        GeneralUserDaoFactory gudf = null;

        if (userBean.getUsername().equals("")) {
            throw new EmptyFieldException("Username necessary");
        }
        if (userBean.getPassword().equals("")) {
            throw new EmptyFieldException("Password necessary");
        }

        switch (view) {
            case "gui" -> gudf = new GeneralUserDao();
            case "cli" -> gudf = new GeneralUserDaoL();
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
        switch (view) {
            case "gui" -> udf = new UserDao();
            case "cli" -> udf = new UserDaoL();
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
        switch (view) {
            case "gui" -> tgdf = new TouristGuideDao();
            case "cli" -> tgdf = new TouristGuideDaoL();
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

