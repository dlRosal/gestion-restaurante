package org.example.pedidosrestaurantes.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.pedidosrestaurantes.DatabaseConnection;
import org.example.pedidosrestaurantes.modelos.Pedido;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class PedidosController {
    @FXML
    private TextField txtIdPedido, txtHoraPedido, txtCantidad, txtTotal;
    @FXML
    private ComboBox<String> cmbCliente, cmbEstado, cmbProducto;
    @FXML
    private DatePicker dpFechaPedido;
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

    private Connection conexion;

    public void initialize() {
        colIDPedido.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colFechaPedido.setCellValueFactory(new PropertyValueFactory<>("fechaPedido"));
        colHoraPedido.setCellValueFactory(new PropertyValueFactory<>("horaPedido"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        conexion = DatabaseConnection.getConnection();
        if (conexion == null) {
            System.out.println("Error: No se pudo establecer la conexión con la base de datos.");
            return;
        }
        cargarClientes();
        cargarProductos();
        cargarEstados();
        cargarPedidos();
    }


    private void cargarClientes() {
        ObservableList<String> clientes = FXCollections.observableArrayList();
        String sql = "SELECT nombre FROM Clientes";
        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clientes.add(rs.getString("nombre"));
            }
            cmbCliente.setItems(clientes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarProductos() {
        ObservableList<String> productos = FXCollections.observableArrayList();
        String sql = "SELECT nombre FROM Productos WHERE disponibilidad = 1";
        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                productos.add(rs.getString("nombre"));
            }
            cmbProducto.setItems(productos);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarEstados() {
        cmbEstado.setItems(FXCollections.observableArrayList("Pendiente", "En preparación", "Entregado"));
    }
    private int obtenerIdProducto(String nombreProducto) {
        String sql = "SELECT id FROM Productos WHERE nombre = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, nombreProducto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private double obtenerPrecioProducto(int idProducto) {
        String sql = "SELECT precio FROM Productos WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("precio");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }




    private void guardarDetallePedido(int idPedido) {
        if (cmbProducto.getValue() == null || txtCantidad.getText().trim().isEmpty()) {
            System.out.println("Error: Debes seleccionar un producto y especificar una cantidad.");
            return;
        }

        try {
            int idProducto = obtenerIdProducto(cmbProducto.getValue()); // Obtener ID del producto
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            double precio = obtenerPrecioProducto(idProducto); // Obtener precio del producto

            String sql = "INSERT INTO DetallePedidos (idPedido, idProducto, cantidad, precio, subtotal) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setInt(1, idPedido);
                stmt.setInt(2, idProducto);
                stmt.setInt(3, cantidad);
                stmt.setDouble(4, precio);
                stmt.setDouble(5, cantidad * precio); // Calculamos el subtotal
                stmt.executeUpdate();
            }

            System.out.println("Producto agregado al pedido correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al guardar el detalle del pedido.");
        }
    }


    private void cargarPedidos() {
        ObservableList<Pedido> listaPedidos = FXCollections.observableArrayList();
        String sql = "SELECT p.idPedido, c.nombre AS cliente, p.fechaPedido, p.horaPedido, " +
                "IFNULL((SELECT SUM(d.cantidad * pr.precio) FROM DetallePedidos d " +
                "INNER JOIN Productos pr ON d.idProducto = pr.id " +
                "WHERE d.idPedido = p.idPedido), 0) AS total, p.estado " +
                "FROM Pedidos p INNER JOIN Clientes c ON p.idCliente = c.id";

        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getInt("idPedido"),
                        rs.getString("cliente"),
                        rs.getDate("fechaPedido").toLocalDate(),
                        rs.getTime("horaPedido").toLocalTime(),
                        rs.getDouble("total"),
                        rs.getString("estado")
                );
                listaPedidos.add(pedido);
            }
            tablePedidos.setItems(listaPedidos);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void guardarPedido() {
        if (cmbCliente.getValue() == null || dpFechaPedido.getValue() == null ||
                txtHoraPedido.getText().trim().isEmpty() || cmbEstado.getValue() == null) {
            System.out.println("Error: Todos los campos son obligatorios.");
            return;
        }

        try {
            int idCliente = obtenerIdCliente(cmbCliente.getValue());
            if (idCliente == -1) {
                System.out.println("Error: No se puede crear el pedido porque el cliente no existe.");
                return;
            }

            LocalTime horaPedido = LocalTime.parse(txtHoraPedido.getText().trim());

            String sql = "INSERT INTO Pedidos (idCliente, fechaPedido, horaPedido, total, estado) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, idCliente);
                stmt.setDate(2, Date.valueOf(dpFechaPedido.getValue()));
                stmt.setTime(3, Time.valueOf(horaPedido));
                stmt.setDouble(4, 0.0); // El total se calculará después de agregar productos
                stmt.setString(5, cmbEstado.getValue());
                stmt.executeUpdate();

                // Obtener el ID del pedido recién insertado
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int idPedido = rs.getInt(1);
                    guardarDetallePedido(idPedido); // Guardar productos en DetallePedidos

                    // Actualizar el total del pedido después de insertar los productos
                    actualizarTotalPedido(idPedido);
                }
            }

            System.out.println("Pedido guardado exitosamente.");
            cargarPedidos();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al guardar el pedido en la base de datos.");
        }
    }
    private void actualizarTotalPedido(int idPedido) {
        String sql = "UPDATE Pedidos SET total = (SELECT SUM(d.cantidad * p.precio) " +
                "FROM DetallePedidos d INNER JOIN Productos p ON d.idProducto = p.id " +
                "WHERE d.idPedido = ?) WHERE idPedido = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);
            stmt.setInt(2, idPedido);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    @FXML
    private void buscarPedido() {
        if (txtIdPedido.getText().trim().isEmpty()) {
            System.out.println("Error: Introduce un ID de pedido.");
            return;
        }

        try {
            int idPedido = Integer.parseInt(txtIdPedido.getText().trim());

            // Obtener datos del pedido desde la tabla Pedidos
            String sqlPedido = "SELECT p.idPedido, c.nombre AS cliente, p.fechaPedido, p.horaPedido, p.estado, p.total " +
                    "FROM Pedidos p INNER JOIN Clientes c ON p.idCliente = c.id " +
                    "WHERE p.idPedido = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(sqlPedido)) {
                stmt.setInt(1, idPedido);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    cmbCliente.setValue(rs.getString("cliente")); // Autocompletar cliente
                    dpFechaPedido.setValue(rs.getDate("fechaPedido").toLocalDate());
                    txtHoraPedido.setText(rs.getTime("horaPedido").toString());
                    cmbEstado.setValue(rs.getString("estado"));
                    txtTotal.setText(String.valueOf(rs.getDouble("total")));
                } else {
                    System.out.println("Error: No se encontró el pedido.");
                    return;
                }
            }

            // Obtener datos del producto desde la tabla DetallePedidos
            String sqlDetalle = "SELECT pr.nombre AS producto, d.cantidad " +
                    "FROM DetallePedidos d INNER JOIN Productos pr ON d.idProducto = pr.id " +
                    "WHERE d.idPedido = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(sqlDetalle)) {
                stmt.setInt(1, idPedido);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    cmbProducto.setValue(rs.getString("producto")); // Autocompletar producto
                    txtCantidad.setText(String.valueOf(rs.getInt("cantidad"))); // Autocompletar cantidad
                } else {
                    System.out.println("Advertencia: No hay productos asociados a este pedido.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al buscar el pedido.");
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID del pedido debe ser un número.");
        }
    }


    @FXML
    private void actualizarPedido() {
        String sql = "UPDATE Pedidos SET fechaPedido=?, horaPedido=?, estado=? WHERE idPedido=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(dpFechaPedido.getValue()));
            stmt.setTime(2, Time.valueOf(txtHoraPedido.getText()));
            stmt.setString(3, cmbEstado.getValue());
            stmt.setInt(4, Integer.parseInt(txtIdPedido.getText()));
            stmt.executeUpdate();
            cargarPedidos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarPedido() {
        String sql = "DELETE FROM Pedidos WHERE idPedido = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(txtIdPedido.getText()));
            stmt.executeUpdate();
            cargarPedidos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) tablePedidos.getScene().getWindow();
        stage.close();
    }
    private double calcularTotalPedido() {
        if (txtIdPedido.getText().trim().isEmpty()) {
            System.out.println("Error: No hay un ID de pedido válido para calcular el total.");
            return 0.0;  // Retorna 0 si no hay ID de pedido válido
        }

        double total = 0.0;
        String sql = "SELECT SUM(d.cantidad * p.precio) AS total " +
                "FROM DetallePedidos d " +
                "INNER JOIN Productos p ON d.idProducto = p.id " +
                "WHERE d.idPedido = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(txtIdPedido.getText().trim()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }


    private int obtenerIdCliente(String nombreCliente) {
        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            System.out.println("Error: No se ha seleccionado un cliente.");
            return -1;
        }

        String sql = "SELECT id FROM Clientes WHERE nombre = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, nombreCliente);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                System.out.println("Error: Cliente no encontrado en la base de datos.");
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    private void guardarProductoEnPedido(int idPedido, int idProducto, int cantidad, double precio, double subtotal) {
        String sql = "INSERT INTO DetallePedidos (idPedido, idProducto, cantidad, precio, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);
            stmt.setInt(2, idProducto);
            stmt.setInt(3, cantidad);
            stmt.setDouble(4, precio);
            stmt.setDouble(5, subtotal);
            stmt.executeUpdate();

            mostrarAlerta("Éxito", "Producto agregado al pedido correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo agregar el producto al pedido.");
        }
    }


    @FXML
    private void agregarProducto() {
        // Validar que haya un producto seleccionado y cantidad ingresada
        String productoSeleccionado = cmbProducto.getValue();
        if (productoSeleccionado == null || txtCantidad.getText().isEmpty()) {
            mostrarAlerta("Error", "Debe seleccionar un producto y una cantidad válida.");
            return;
        }

        // Obtener la cantidad ingresada y validarla
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) {
                mostrarAlerta("Error", "La cantidad debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Ingrese un número válido en la cantidad.");
            return;
        }

        // Obtener el ID del pedido seleccionado
        int idPedido;
        try {
            idPedido = Integer.parseInt(txtIdPedido.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Debe seleccionar un pedido válido antes de agregar productos.");
            return;
        }

        // Obtener el ID del producto usando su nombre
        int idProducto = obtenerIdProducto(productoSeleccionado);
        if (idProducto == -1) {
            mostrarAlerta("Error", "No se pudo encontrar el ID del producto.");
            return;
        }

        // Obtener el precio del producto usando el ID
        double precio = obtenerPrecioProducto(idProducto);
        double subtotal = cantidad * precio;

        // Guardar el producto en la base de datos
        guardarProductoEnPedido(idPedido, idProducto, cantidad, precio, subtotal);

        // Actualizar el total del pedido
        actualizarTotalPedido(idPedido);

    }
    @FXML
    private void eliminarProducto() {
        // Obtener el producto seleccionado del ComboBox
        String productoSeleccionado = cmbProducto.getValue();
        if (productoSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un producto para eliminar.");
            return;
        }

        // Obtener el ID del pedido desde el campo de texto
        int idPedido;
        try {
            idPedido = Integer.parseInt(txtIdPedido.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Debe seleccionar un pedido válido antes de eliminar productos.");
            return;
        }

        // Obtener el ID del producto seleccionado
        int idProducto = obtenerIdProducto(productoSeleccionado);
        if (idProducto == -1) {
            mostrarAlerta("Error", "No se pudo encontrar el ID del producto.");
            return;
        }

        // Consulta SQL para eliminar el producto del pedido
        String sql = "DELETE FROM DetallePedidos WHERE idPedido = ? AND idProducto = ? LIMIT 1";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);
            stmt.setInt(2, idProducto);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarAlerta("Éxito", "Producto eliminado del pedido correctamente.");

                // 🔥 🔥 🔥 Llamamos a actualizarTotalPedido para recalcular el total después de eliminar
                actualizarTotalPedido(idPedido);
            } else {
                mostrarAlerta("Error", "El producto no se encontró en el pedido.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo eliminar el producto del pedido.");
        }
    }



    @FXML
    private void verDetallesPedido() {
        Pedido pedidoSeleccionado = tablePedidos.getSelectionModel().getSelectedItem();
        if (pedidoSeleccionado == null) {
            System.out.println("Error: Selecciona un pedido para ver sus detalles.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pedidosrestaurantes/detalle_pedido.fxml"));
            Parent root = loader.load();

            // Pasar el ID del pedido a DetallePedidosController
            DetallePedidosController controller = loader.getController();
            controller.cargarDetallesPedido(pedidoSeleccionado.getIdPedido());

            Stage stage = new Stage();
            stage.setTitle("Detalles del Pedido");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}