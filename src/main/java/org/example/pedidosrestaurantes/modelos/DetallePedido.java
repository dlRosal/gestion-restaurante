package org.example.pedidosrestaurantes.modelos;

import javafx.beans.property.*;

public class DetallePedido {
    private final IntegerProperty idDetalle;
    private final IntegerProperty idPedido;
    private final IntegerProperty idProducto;
    private final IntegerProperty cantidad;
    private final DoubleProperty precio;
    private final DoubleProperty subtotal;

    public DetallePedido(int idDetalle, int idPedido, int idProducto, int cantidad, double precio) {
        this.idDetalle = new SimpleIntegerProperty(idDetalle);
        this.idPedido = new SimpleIntegerProperty(idPedido);
        this.idProducto = new SimpleIntegerProperty(idProducto);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.precio = new SimpleDoubleProperty(precio);
        this.subtotal = new SimpleDoubleProperty(cantidad * precio);
    }

    // Getters y Setters con propiedades
    public IntegerProperty idDetalleProperty() { return idDetalle; }
    public IntegerProperty idPedidoProperty() { return idPedido; }
    public IntegerProperty idProductoProperty() { return idProducto; }
    public IntegerProperty cantidadProperty() { return cantidad; }
    public DoubleProperty precioProperty() { return precio; }
    public DoubleProperty subtotalProperty() { return subtotal; }

    public int getIdDetalle() { return idDetalle.get(); }
    public void setIdDetalle(int idDetalle) { this.idDetalle.set(idDetalle); }

    public int getIdPedido() { return idPedido.get(); }
    public void setIdPedido(int idPedido) { this.idPedido.set(idPedido); }

    public int getIdProducto() { return idProducto.get(); }
    public void setIdProducto(int idProducto) { this.idProducto.set(idProducto); }

    public int getCantidad() { return cantidad.get(); }
    public void setCantidad(int cantidad) {
        this.cantidad.set(cantidad);
        this.subtotal.set(cantidad * getPrecio()); // Recalcular subtotal
    }

    public double getPrecio() { return precio.get(); }
    public void setPrecio(double precio) {
        this.precio.set(precio);
        this.subtotal.set(getCantidad() * precio); // Recalcular subtotal
    }

    public double getSubtotal() { return subtotal.get(); }
}
