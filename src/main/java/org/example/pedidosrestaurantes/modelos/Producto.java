package org.example.pedidosrestaurantes.modelos;

import javafx.beans.property.*;

public class Producto {
    private final IntegerProperty id;
    private final StringProperty nombre;
    private final StringProperty categoria;
    private final DoubleProperty precio;
    private final BooleanProperty disponibilidad;

    public Producto(int id, String nombre, String categoria, double precio, boolean disponibilidad) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.categoria = new SimpleStringProperty(categoria);
        this.precio = new SimpleDoubleProperty(precio);
        this.disponibilidad = new SimpleBooleanProperty(disponibilidad);
    }

    // Getters y setters con propiedades
    public IntegerProperty idProperty() { return id; }
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty categoriaProperty() { return categoria; }
    public DoubleProperty precioProperty() { return precio; }
    public BooleanProperty disponibilidadProperty() { return disponibilidad; }

}
