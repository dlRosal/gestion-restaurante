package org.example.pedidosrestaurantes.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    public void mostrarClientes() throws IOException {
        // Cargar la vista de clientes en una nueva ventana
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pedidosrestaurantes/clientes.fxml"));
        Parent root = loader.load();

        Stage clienteStage = new Stage();
        clienteStage.setTitle("Gestión de Clientes");
        clienteStage.setScene(new Scene(root, 1200, 900));
        clienteStage.show();
    }

    @FXML
    public void mostrarProductos() throws IOException {
        // Cargar la vista de productos en una nueva ventana
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pedidosrestaurantes/productos.fxml"));
        Parent root = loader.load();

        Stage productosStage = new Stage();
        productosStage.setTitle("Gestión de Productos");
        productosStage.setScene(new Scene(root, 1200, 900));
        productosStage.show();
    }

    @FXML
    public void mostrarPedidos() throws IOException {
        // Cargar la vista de pedidos en una nueva ventana
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pedidosrestaurantes/pedidos.fxml"));
        Parent root = loader.load();

        Stage pedidosStage = new Stage();
        pedidosStage.setTitle("Gestión de Pedidos");
        pedidosStage.setScene(new Scene(root, 1200, 900));
        pedidosStage.show();
    }
}
