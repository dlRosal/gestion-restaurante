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
    private TableColumn<DetallePedido, Integer> colIDDetalle;
    @FXML
    private TableColumn<DetallePedido, String> colProducto;
    @FXML
    private TableColumn<DetallePedido, Integer> colCantidad;
    @FXML
    private TableColumn<DetallePedido, Double> colPrecio;
    @FXML
    private TableColumn<DetallePedido, Double> colSubtotal;

    private Connection conexion;
    private int idPedido;

    /**
     * Método de inicialización que configura la conexión a la base de datos y las columnas de la tabla.
     */
    @FXML
    public void initialize() {
        conexion = DatabaseConnection.getConnection();
        if (conexion == null) {
            System.out.println("Error: No se pudo establecer la conexión con la base de datos.");
            return;
        }

        // Configurar las columnas de la TableView
        colIDDetalle.setCellValueFactory(cellData -> cellData.getValue().idDetallePedidoProperty().asObject());
        colProducto.setCellValueFactory(cellData -> cellData.getValue().productoProperty());
        colCantidad.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty().asObject());
        colPrecio.setCellValueFactory(cellData -> cellData.getValue().precioProperty().asObject());
        colSubtotal.setCellValueFactory(cellData -> cellData.getValue().subtotalProperty().asObject());
    }

    /**
     * Carga los detalles de un pedido específico en la tabla.
     * @param idPedido Identificador del pedido.
     */
    public void cargarDetallesPedido(int idPedido) {
        this.idPedido = idPedido;
        ObservableList<DetallePedido> listaDetalles = FXCollections.observableArrayList();

        String sql = "SELECT d.idDetallePedido, p.nombre AS nombre_producto, d.cantidad, d.precio, d.subtotal " +
                "FROM DetallePedidos d " +
                "INNER JOIN Productos p ON d.idProducto = p.id " +
                "WHERE d.idPedido = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DetallePedido detalle = new DetallePedido(
                        rs.getInt("idDetallePedido"),
                        rs.getString("nombre_producto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio"),
                        rs.getDouble("subtotal")
                );
                listaDetalles.add(detalle);
            }

            if (listaDetalles.isEmpty()) {
                System.out.println("Advertencia: No hay productos asociados a este pedido.");
            } else {
                System.out.println("Detalles del pedido cargados correctamente.");
            }

            tableDetallesPedido.setItems(listaDetalles);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al cargar los detalles del pedido.");
        }
    }

    /**
     * Cierra la ventana actual.
     */
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) tableDetallesPedido.getScene().getWindow();
        stage.close();
    }
}
