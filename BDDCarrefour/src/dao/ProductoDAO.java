package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Producto;
import config.DatabaseConnection;

public class ProductoDAO implements IProductoDAO {
    private Connection connection;

    public ProductoDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public boolean crearProducto(Producto producto) {
        String sql = "INSERT INTO Productos (nombre, descripcion, precio, stock) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Producto obtenerProductoPorId(int id) {
        String sql = "SELECT * FROM Productos WHERE producto_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Producto(
                        rs.getInt("producto_id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener producto: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Productos";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("producto_id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                );
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }
        return productos;
    }

    @Override
    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE Productos SET nombre = ?, descripcion = ?, precio = ?, stock = ? WHERE producto_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());
            stmt.setInt(5, producto.getProductoId());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarProducto(int productoId) {
        String sql = "DELETE FROM productos WHERE producto_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productoId);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {
                // Error de clave foránea
                System.err.println("No se puede eliminar el producto porque tiene ventas asociadas.");
            } else {
                System.err.println("Error al eliminar el producto: " + e.getMessage());
            }
            return false;
        }
    }

    @Override
    public List<Producto> buscarProductosPorNombre(String nombre) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Productos WHERE nombre LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("producto_id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                );
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos: " + e.getMessage());
        }
        return productos;
    }

    @Override
    public boolean actualizarStock(int productoId, int nuevoStock) {
        String sql = "UPDATE Productos SET stock = ? WHERE producto_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, nuevoStock);
            stmt.setInt(2, productoId);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }
} 