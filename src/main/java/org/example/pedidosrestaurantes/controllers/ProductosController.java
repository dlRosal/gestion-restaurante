package org.example.pedidosrestaurantes.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.pedidosrestaurantes.DatabaseConnection;
import org.example.pedidosrestaurantes.modelos.Producto;

import java.sql.*;

public class ProductosController {

    @FXML
    private TextField txtId, txtNombre, txtCategoria, txtPrecio;
    @FXML
    private CheckBox chkDisponibilidad;
    @FXML
    private TableView<Producto> tableProductos;
    @FXML
    private TableColumn<Producto, Integer> colID;
    @FXML
    private TableColumn<Producto, String> colNombre, colCategoria;
    @FXML
    private TableColumn<Producto, Double> colPrecio;
    @FXML
    private TableColumn<Producto, Boolean> colDisponibilidad;

    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    public void initialize() {
        colID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colCategoria.setCellValueFactory(cellData -> cellData.getValue().categoriaProperty());
        colPrecio.setCellValueFactory(cellData -> cellData.getValue().precioProperty().asObject());
        colDisponibilidad.setCellValueFactory(cellData -> cellData.getValue().disponibilidadProperty().asObject());
        cargarProductos();
    }

    /**
     * Metodo para cerrar la ventana y volver al menú principal
     */
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtId.getScene().getWindow();
        stage.close();
    }

    /**
     * Cargar todos los productos desde la base de datos en la TableView
     */
    private void cargarProductos() {
        listaProductos.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM productos")) {
            while (rs.next()) {
                listaProductos.add(new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getBoolean("disponibilidad")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableProductos.setItems(listaProductos);
    }

    /**
     * Método para guardar un nuevo producto en la base de datos
     */
    @FXML
    private void guardarProducto() {
        String query = "INSERT INTO productos (nombre, categoria, precio, disponibilidad) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, txtNombre.getText());
            stmt.setString(2, txtCategoria.getText());
            stmt.setDouble(3, Double.parseDouble(txtPrecio.getText()));
            stmt.setBoolean(4, chkDisponibilidad.isSelected());
            stmt.executeUpdate();
            cargarProductos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para buscar un producto por su ID
     */
    @FXML
    private void buscarProducto() {
        String query = "SELECT * FROM productos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                txtNombre.setText(rs.getString("nombre"));
                txtCategoria.setText(rs.getString("categoria"));
                txtPrecio.setText(String.valueOf(rs.getDouble("precio")));
                chkDisponibilidad.setSelected(rs.getBoolean("disponibilidad"));
            } else {
                System.out.println("Producto no encontrado No se encontró un producto con ese ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo para actualizar un producto en la base de datos
     */
    @FXML
    private void actualizarProducto() {
        String query = "UPDATE productos SET nombre=?, categoria=?, precio=?, disponibilidad=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, txtNombre.getText());
            stmt.setString(2, txtCategoria.getText());
            stmt.setDouble(3, Double.parseDouble(txtPrecio.getText()));
            stmt.setBoolean(4, chkDisponibilidad.isSelected());
            stmt.setInt(5, Integer.parseInt(txtId.getText()));
            stmt.executeUpdate();
            cargarProductos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo para eliminar un producto de la base de datos
     */
    @FXML
    private void eliminarProducto() {
        String query = "DELETE FROM productos WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.executeUpdate();
            cargarProductos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
