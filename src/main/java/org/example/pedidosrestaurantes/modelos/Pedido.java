package org.example.pedidosrestaurantes.modelos;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class Pedido {
    private final IntegerProperty idPedido;
    private final IntegerProperty idCliente;
    private final ObjectProperty<LocalDate> fechaPedido;
    private final ObjectProperty<LocalTime> horaPedido;
    private final DoubleProperty total;
    private final StringProperty estado;

    public Pedido(int idPedido, int idCliente, LocalDate fechaPedido, LocalTime horaPedido, double total, String estado) {
        this.idPedido = new SimpleIntegerProperty(idPedido);
        this.idCliente = new SimpleIntegerProperty(idCliente);
        this.fechaPedido = new SimpleObjectProperty<>(fechaPedido);
        this.horaPedido = new SimpleObjectProperty<>(horaPedido);
        this.total = new SimpleDoubleProperty(total);
        this.estado = new SimpleStringProperty(estado);
    }

    // Getters y Setters con propiedades
    public IntegerProperty idPedidoProperty() { return idPedido; }
    public IntegerProperty idClienteProperty() { return idCliente; }
    public ObjectProperty<LocalDate> fechaPedidoProperty() { return fechaPedido; }
    public ObjectProperty<LocalTime> horaPedidoProperty() { return horaPedido; }
    public DoubleProperty totalProperty() { return total; }
    public StringProperty estadoProperty() { return estado; }

    public int getIdPedido() { return idPedido.get(); }
    public void setIdPedido(int idPedido) { this.idPedido.set(idPedido); }

    public int getIdCliente() { return idCliente.get(); }
    public void setIdCliente(int idCliente) { this.idCliente.set(idCliente); }

    public LocalDate getFechaPedido() { return fechaPedido.get(); }
    public void setFechaPedido(LocalDate fechaPedido) { this.fechaPedido.set(fechaPedido); }

    public LocalTime getHoraPedido() { return horaPedido.get(); }
    public void setHoraPedido(LocalTime horaPedido) { this.horaPedido.set(horaPedido); }

    public double getTotal() { return total.get(); }
    public void setTotal(double total) { this.total.set(total); }

    public String getEstado() { return estado.get(); }
    public void setEstado(String estado) { this.estado.set(estado); }
}
