package it.ispw.daniele.backpacker.utils;

import java.io.File;

public class FileManager {
    private FileManager() {
    }

    private static final String PROJECT = System.getProperty("user.home") + File.separator
            + "Desktop" + File.separator + "ISPW_Project" + File.separator + "trunk" + File.separator
            + "Project" + File.separator + "src" + File.separator + "main" + File.separator
            + "resources" + File.separator;

    public static final String PROFILE = PROJECT + "profilePictures";

}
