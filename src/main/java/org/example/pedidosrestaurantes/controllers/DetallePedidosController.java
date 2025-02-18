package org.example.pedidosrestaurantes.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.pedidosrestaurantes.DatabaseConnection;
import org.example.pedidosrestaurantes.modelos.DetallePedido;

import java.sql.*;

public class DetallePedidosController {

    @FXML
    private TableView<DetallePedido> tableDetallesPedido;
    @FXML
    private TableColumn<DetallePedido, Integer> colIDDetalle, colCantidad;
    @FXML
    private TableColumn<DetallePedido, String> colProducto;
    @FXML
    private TableColumn<DetallePedido, Double> colPrecio, colSubtotal;

    private ObservableList<DetallePedido> listaDetalles = FXCollections.observableArrayList();
    private int idPedido;  // Se recibe desde PedidosController

    public void initialize() {
        colIDDetalle.setCellValueFactory(cellData -> cellData.getValue().idDetalleProperty().asObject());
        colProducto.setCellValueFactory(cellData -> cellData.getValue().idProductoProperty().asString());
        colCantidad.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty().asObject());
        colPrecio.setCellValueFactory(cellData -> cellData.getValue().precioProperty().asObject());
        colSubtotal.setCellValueFactory(cellData -> cellData.getValue().subtotalProperty().asObject());

        tableDetallesPedido.setItems(listaDetalles);
    }

    /**
     * Cargar los detalles del pedido seleccionado.
     * @param idPedido ID del pedido a consultar.
     */
    public void cargarDetallesPedido(int idPedido) {
        this.idPedido = idPedido;
        listaDetalles.clear();

        String query = "SELECT d.id_detalle, d.id_producto, p.nombre, d.cantidad, d.precio, (d.cantidad * d.precio) AS subtotal " +
                "FROM DetallePedidos d " +
                "JOIN Productos p ON d.id_producto = p.id " +
                "WHERE d.id_pedido = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idPedido);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                listaDetalles.add(new DetallePedido(
                        rs.getInt("id_detalle"),
                        idPedido,
                        rs.getInt("id_producto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cerrar la ventana y regresar a la gestión de pedidos.
     */
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) tableDetallesPedido.getScene().getWindow();
        stage.close();
    }
}
