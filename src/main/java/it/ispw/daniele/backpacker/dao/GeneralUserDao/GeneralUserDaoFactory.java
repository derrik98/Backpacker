package it.ispw.daniele.backpacker.dao.GeneralUserDao;

import it.ispw.daniele.backpacker.dao.DaoTemplate;
import it.ispw.daniele.backpacker.entity.GeneralUser;

public abstract class GeneralUserDaoFactory extends DaoTemplate {

    public abstract GeneralUser findUser(String username, String password);

}
