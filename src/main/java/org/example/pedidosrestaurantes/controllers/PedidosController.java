package org.example.pedidosrestaurantes.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.pedidosrestaurantes.DatabaseConnection;
import org.example.pedidosrestaurantes.modelos.Pedido;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class PedidosController {

    @FXML
    private TextField txtIdPedido, txtHoraPedido, txtTotal;
    @FXML
    private DatePicker dpFechaPedido;
    @FXML
    private ComboBox<String> cmbCliente, cmbEstado;
    @FXML
    private TableView<Pedido> tablePedidos;
    @FXML
    private TableColumn<Pedido, Integer> colIDPedido;
    @FXML
    private TableColumn<Pedido, String> colCliente;
    @FXML
    private TableColumn<Pedido, LocalDate> colFechaPedido;
    @FXML
    private TableColumn<Pedido, LocalTime> colHoraPedido;
    @FXML
    private TableColumn<Pedido, String> colEstado;
    @FXML
    private TableColumn<Pedido, Double> colTotal;

    private ObservableList<Pedido> listaPedidos = FXCollections.observableArrayList();
    private ObservableList<String> listaClientes = FXCollections.observableArrayList();
    private ObservableList<String> listaEstados = FXCollections.observableArrayList("Pendiente", "En preparación", "Entregado");

    public void initialize() {
        cmbEstado.setItems(listaEstados);
        cargarClientes();
        cargarPedidos();

        colIDPedido.setCellValueFactory(cellData -> cellData.getValue().idPedidoProperty().asObject());
        colCliente.setCellValueFactory(cellData -> cellData.getValue().idClienteProperty().asString());
        colFechaPedido.setCellValueFactory(cellData -> cellData.getValue().fechaPedidoProperty());
        colHoraPedido.setCellValueFactory(cellData -> cellData.getValue().horaPedidoProperty());
        colEstado.setCellValueFactory(cellData -> cellData.getValue().estadoProperty());
        colTotal.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());

        tablePedidos.setItems(listaPedidos);
    }

    /**
     * Cargar los clientes en el ComboBox desde la base de datos.
     */
    private void cargarClientes() {
        listaClientes.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, nombre FROM Clientes")) {
            while (rs.next()) {
                listaClientes.add(rs.getInt("id") + " - " + rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cmbCliente.setItems(listaClientes);
    }

    /**
     * Cargar los pedidos desde la base de datos en la TableView.
     */
    private void cargarPedidos() {
        listaPedidos.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Pedidos")) {
            while (rs.next()) {
                listaPedidos.add(new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getInt("id_cliente"),
                        rs.getDate("fecha_pedido").toLocalDate(),
                        rs.getTime("hora_pedido").toLocalTime(),
                        rs.getDouble("total"),
                        rs.getString("estado")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tablePedidos.setItems(listaPedidos);
    }

    /**
     * Método para guardar un nuevo pedido en la base de datos.
     */
    @FXML
    private void guardarPedido() {
        String query = "INSERT INTO Pedidos (id_cliente, fecha_pedido, hora_pedido, total, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int idCliente = Integer.parseInt(cmbCliente.getValue().split(" - ")[0]);

            stmt.setInt(1, idCliente);
            stmt.setDate(2, Date.valueOf(dpFechaPedido.getValue()));
            stmt.setTime(3, Time.valueOf(txtHoraPedido.getText()));
            stmt.setDouble(4, 0.00); // El total se calculará con los detalles
            stmt.setString(5, cmbEstado.getValue());
            stmt.executeUpdate();

            cargarPedidos();
            limpiarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para buscar un pedido por su ID.
     */
    @FXML
    private void buscarPedido() {
        String query = "SELECT * FROM Pedidos WHERE id_pedido = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(txtIdPedido.getText()));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dpFechaPedido.setValue(rs.getDate("fecha_pedido").toLocalDate());
                txtHoraPedido.setText(rs.getTime("hora_pedido").toString());
                cmbCliente.setValue(rs.getInt("id_cliente") + " - (Cliente)");
                txtTotal.setText(String.valueOf(rs.getDouble("total")));
                cmbEstado.setValue(rs.getString("estado"));
            } else {
                mostrarAlerta("Pedido no encontrado", "No se encontró un pedido con ese ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para actualizar un pedido en la base de datos.
     */
    @FXML
    private void actualizarPedido() {
        String query = "UPDATE Pedidos SET id_cliente=?, fecha_pedido=?, hora_pedido=?, estado=? WHERE id_pedido=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int idCliente = Integer.parseInt(cmbCliente.getValue().split(" - ")[0]);

            stmt.setInt(1, idCliente);
            stmt.setDate(2, Date.valueOf(dpFechaPedido.getValue()));
            stmt.setTime(3, Time.valueOf(txtHoraPedido.getText()));
            stmt.setString(4, cmbEstado.getValue());
            stmt.setInt(5, Integer.parseInt(txtIdPedido.getText()));

            stmt.executeUpdate();
            cargarPedidos();
            limpiarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para eliminar un pedido de la base de datos.
     */
    @FXML
    private void eliminarPedido() {
        String query = "DELETE FROM Pedidos WHERE id_pedido=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(txtIdPedido.getText()));
            stmt.executeUpdate();
            cargarPedidos();
            limpiarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para limpiar los campos del formulario.
     */
    private void limpiarCampos() {
        txtIdPedido.clear();
        cmbCliente.setValue(null);
        dpFechaPedido.setValue(null);
        txtHoraPedido.clear();
        txtTotal.clear();
        cmbEstado.setValue(null);
    }

    /**
     * Método para cerrar la ventana de pedidos.
     */
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtIdPedido.getScene().getWindow();
        stage.close();
    }

    /**
     * Método para mostrar alertas.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
