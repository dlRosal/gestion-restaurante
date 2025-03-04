module org.example.pedidosrestaurantes {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;
    requires net.sf.jasperreports.core;

    opens org.example.pedidosrestaurantes to javafx.fxml;
    exports org.example.pedidosrestaurantes;
    exports org.example.pedidosrestaurantes.modelos;
    opens org.example.pedidosrestaurantes.modelos to javafx.fxml;
    exports org.example.pedidosrestaurantes.controllers;
    opens org.example.pedidosrestaurantes.controllers to javafx.fxml;
}