package org.example.pedidosrestaurantes;

import javafx.beans.property.*;

public class Cliente {
    private final IntegerProperty id;
    private final StringProperty nombre;
    private final StringProperty telefono;
    private final StringProperty direccion;

    public Cliente(int id, String nombre, String telefono, String direccion) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.telefono = new SimpleStringProperty(telefono);
        this.direccion = new SimpleStringProperty(direccion);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty telefonoProperty() { return telefono; }
    public StringProperty direccionProperty() { return direccion; }
}
