package org.example.pedidosrestaurantes.modelos;

import javafx.beans.property.*;

public class DetallePedido {
    private final IntegerProperty idDetallePedido;
    private final StringProperty producto;
    private final IntegerProperty cantidad;
    private final DoubleProperty precio;
    private final DoubleProperty subtotal;

    public DetallePedido(int idDetallePedido, String producto, int cantidad, double precio, double subtotal) {
        this.idDetallePedido = new SimpleIntegerProperty(idDetallePedido);
        this.producto = new SimpleStringProperty(producto);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.precio = new SimpleDoubleProperty(precio);
        this.subtotal = new SimpleDoubleProperty(subtotal);
    }

    public int getIdDetallePedido() {
        return idDetallePedido.get();
    }

    public IntegerProperty idDetallePedidoProperty() {
        return idDetallePedido;
    }

    public String getProducto() {
        return producto.get();
    }

    public StringProperty productoProperty() {
        return producto;
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public IntegerProperty cantidadProperty() {
        return cantidad;
    }

    public double getPrecio() {
        return precio.get();
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    public double getSubtotal() {
        return subtotal.get();
    }

    public DoubleProperty subtotalProperty() {
        return subtotal;
    }
}
