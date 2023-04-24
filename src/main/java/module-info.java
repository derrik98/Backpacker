module it.ispw.daniele.backpacker {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires java.logging;
    requires java.sql;
    requires org.jetbrains.annotations;
    requires json.simple;
    requires org.json;

    exports it.ispw.daniele.backpacker.utils;
    exports it.ispw.daniele.backpacker.entity;
    exports it.ispw.daniele.backpacker.bean;
    exports it.ispw.daniele.backpacker.exceptions;
    exports it.ispw.daniele.backpacker.view.fxml_view;
    opens it.ispw.daniele.backpacker.view.fxml_view to javafx.fxml;
    exports it.ispw.daniele.backpacker.view.command_line_interface;
    opens it.ispw.daniele.backpacker.view.command_line_interface to javafx.fxml;


}