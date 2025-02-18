package org.example.pedidosrestaurantes.modelos;

import java.time.LocalDate;
import java.time.LocalTime;

public class Pedido {
    private int idPedido;
    private String cliente;
    private LocalDate fechaPedido;
    private LocalTime horaPedido;
    private double total;
    private String estado;

    public Pedido(int idPedido, String cliente, LocalDate fechaPedido, LocalTime horaPedido, double total, String estado) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.fechaPedido = fechaPedido;
        this.horaPedido = horaPedido;
        this.total = total;
        this.estado = estado;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public LocalTime getHoraPedido() {
        return horaPedido;
    }

    public void setHoraPedido(LocalTime horaPedido) {
        this.horaPedido = horaPedido;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
