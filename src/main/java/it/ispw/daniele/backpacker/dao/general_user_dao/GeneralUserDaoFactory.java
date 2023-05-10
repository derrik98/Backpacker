package it.ispw.daniele.backpacker.dao.general_user_dao;

import it.ispw.daniele.backpacker.dao.DaoTemplate;
import it.ispw.daniele.backpacker.entity.GeneralUser;

public abstract class GeneralUserDaoFactory extends DaoTemplate {

    protected static final String GENERAL_USER = "general_user";
    protected static final String PASSWORD = "password";
    protected static final String USERNAME = "username";
    protected static final String ROLE = "role";

    public abstract GeneralUser findUser(String username, String password);

}
