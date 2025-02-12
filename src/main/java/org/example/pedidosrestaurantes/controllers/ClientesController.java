package org.example.pedidosrestaurantes.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.pedidosrestaurantes.DatabaseConnection;
import org.example.pedidosrestaurantes.modelos.Cliente;

import java.sql.*;

public class ClientesController {

    @FXML
    private TextField txtId, txtNombre, txtTelefono, txtDireccion;
    @FXML
    private TableView<Cliente> tableClientes;
    @FXML
    private TableColumn<Cliente, Integer> colID;
    @FXML
    private TableColumn<Cliente, String> colNombre, colTelefono, colDireccion;

    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    public void initialize() {
        colID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colTelefono.setCellValueFactory(cellData -> cellData.getValue().telefonoProperty());
        colDireccion.setCellValueFactory(cellData -> cellData.getValue().direccionProperty());
        cargarClientes();
    }

    private void cargarClientes() {
        listaClientes.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM clientes")) {
            while (rs.next()) {
                listaClientes.add(new Cliente(rs.getInt("id"), rs.getString("nombre"),
                        rs.getString("telefono"), rs.getString("direccion")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableClientes.setItems(listaClientes);
    }

    @FXML
    private void guardarCliente() {
        String query = "INSERT INTO clientes (nombre, telefono, direccion) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, txtNombre.getText());
            stmt.setString(2, txtTelefono.getText());
            stmt.setString(3, txtDireccion.getText());
            stmt.executeUpdate();
            cargarClientes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void buscarCliente() {
        String query = "SELECT * FROM clientes WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                txtNombre.setText(rs.getString("nombre"));
                txtTelefono.setText(rs.getString("telefono"));
                txtDireccion.setText(rs.getString("direccion"));
            } else {
                System.out.println("Cliente no encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void actualizarCliente() {
        String query = "UPDATE clientes SET nombre=?, telefono=?, direccion=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, txtNombre.getText());
            stmt.setString(2, txtTelefono.getText());
            stmt.setString(3, txtDireccion.getText());
            stmt.setInt(4, Integer.parseInt(txtId.getText()));
            stmt.executeUpdate();
            cargarClientes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarCliente() {
        String query = "DELETE FROM clientes WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.executeUpdate();
            cargarClientes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtId.getScene().getWindow(); // Obtener la ventana actual
        stage.close(); // Cerrar la ventana
    }

}
