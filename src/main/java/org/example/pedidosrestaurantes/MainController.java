package org.example.pedidosrestaurantes;

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
        clienteStage.setScene(new Scene(root, 900, 600));
        clienteStage.show();
    }

    @FXML
    public void mostrarProductos() {
        System.out.println("Gestión de Productos aún no implementada.");
    }

    @FXML
    public void mostrarPedidos() {
        System.out.println("Gestión de Pedidos aún no implementada.");
    }
}
