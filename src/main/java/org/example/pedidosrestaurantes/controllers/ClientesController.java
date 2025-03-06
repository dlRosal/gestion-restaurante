package org.example.pedidosrestaurantes.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.sf.jasperreports.view.JasperViewer;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
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

    /**
     * Método de inicialización que configura las columnas de la tabla y carga los clientes.
     */
    public void initialize() {
        colID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colTelefono.setCellValueFactory(cellData -> cellData.getValue().telefonoProperty());
        colDireccion.setCellValueFactory(cellData -> cellData.getValue().direccionProperty());
        cargarClientes();
    }

    /**
     * Carga los clientes desde la base de datos y los muestra en la tabla.
     */
    private void cargarClientes() {
        listaClientes.clear();
        String query = "SELECT * FROM clientes";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                listaClientes.add(new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("direccion")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableClientes.setItems(listaClientes);
    }

    /**
     * Guarda un nuevo cliente en la base de datos.
     */
    @FXML
    private void guardarCliente() {
        String query = "INSERT INTO clientes (nombre, telefono, direccion) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, txtNombre.getText());
            stmt.setString(2, txtTelefono.getText());
            stmt.setString(3, txtDireccion.getText());
            stmt.executeUpdate();
            cargarClientes(); // Recargar la lista tras la inserción
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Busca un cliente por su ID y muestra sus datos en los campos de texto.
     */
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

    /**
     * Actualiza los datos de un cliente existente en la base de datos.
     */
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
            cargarClientes(); // Recargar la lista tras la actualización
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un cliente de la base de datos.
     */
    @FXML
    private void eliminarCliente() {
        String query = "DELETE FROM clientes WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.executeUpdate();
            cargarClientes(); // Recargar la lista tras la eliminación
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void generarInformeClientes() {
        Connection conn = null;
        try {
            // Ruta del informe (debe ser un archivo .jasper compilado)
            String reportPath = "src/main/resources/Clientes.jasper";

            // Obtener conexión a la base de datos
            conn = DatabaseConnection.getConnection();
            if (conn == null) {
                mostrarAlerta("Error", "No se pudo conectar a la base de datos.");
                return;
            }

            // Parámetros (vacío si no se necesitan)
            Map<String, Object> parameters = new HashMap<>();

            // Llenar el informe con la conexión a la base de datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, parameters, conn);

            // Mostrar el reporte
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo generar el informe de clientes.");
        } finally {
            // Cerrar la conexión si fue abierta
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // Método para mostrar alertas en la UI
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }


    /**
     * Cierra la ventana actual.
     */
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtId.getScene().getWindow();
        stage.close();
    }
}
