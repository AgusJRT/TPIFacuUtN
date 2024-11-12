package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Venta;
import models.DetalleVenta;
import config.DatabaseConnection;

public class VentaDAO implements IVentaDAO {
    private Connection connection;
    private ProductoDAO productoDAO;

    public VentaDAO() {
        this.connection = DatabaseConnection.getConnection();
        this.productoDAO = new ProductoDAO();
    }

    @Override
    public int crearVenta(Venta venta) {
        String sql = "INSERT INTO Ventas (cliente_id, fecha_venta, total) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, venta.getClienteId());
            stmt.setDate(2, venta.getFechaVenta());
            stmt.setDouble(3, venta.getTotal());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int ventaId = rs.getInt(1);
                    // Agregar los detalles de la venta
                    for (DetalleVenta detalle : venta.getDetalles()) {
                        detalle.setVentaId(ventaId);
                        agregarDetalleVenta(detalle);
                    }
                    return ventaId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al crear venta: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public boolean agregarDetalleVenta(DetalleVenta detalle) {
        // Verificar stock antes de agregar el detalle
        var producto = productoDAO.obtenerProductoPorId(detalle.getProductoId());
        if (producto == null || producto.getStock() < detalle.getCantidad()) {
            System.err.println("Stock insuficiente");
            return false;
        }

        String sql = "INSERT INTO Detalles_Ventas (venta_id, producto_id, cantidad, subtotal) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getVentaId());
            stmt.setInt(2, detalle.getProductoId());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getSubtotal());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Actualizar el stock del producto
                int nuevoStock = producto.getStock() - detalle.getCantidad();
                return productoDAO.actualizarStock(detalle.getProductoId(), nuevoStock);
            }
        } catch (SQLException e) {
            System.err.println("Error al agregar detalle de venta: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Venta obtenerVentaPorId(int id) {
        String sql = "SELECT * FROM Ventas WHERE venta_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Venta venta = new Venta(
                        rs.getInt("venta_id"),
                        rs.getInt("cliente_id"),
                        rs.getDate("fecha_venta"),
                        rs.getDouble("total")
                );

                // Obtener los detalles de la venta
                venta.setDetalles(obtenerDetallesVenta(id));
                return venta;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener venta: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<DetalleVenta> obtenerDetallesVenta(int ventaId) {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM Detalles_Ventas WHERE venta_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ventaId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DetalleVenta detalle = new DetalleVenta(
                        rs.getInt("detalle_id"),
                        rs.getInt("venta_id"),
                        rs.getInt("producto_id"),
                        rs.getInt("cantidad"),
                        rs.getDouble("subtotal")
                );
                detalles.add(detalle);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles de venta: " + e.getMessage());
        }
        return detalles;
    }

    @Override
    public List<Venta> obtenerTodasLasVentas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM Ventas";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Venta venta = new Venta(
                        rs.getInt("venta_id"),
                        rs.getInt("cliente_id"),
                        rs.getDate("fecha_venta"),
                        rs.getDouble("total")
                );
                venta.setDetalles(obtenerDetallesVenta(venta.getVentaId()));
                ventas.add(venta);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas: " + e.getMessage());
        }
        return ventas;
    }

    @Override
    public List<Venta> obtenerVentasPorCliente(int clienteId) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM Ventas WHERE cliente_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Venta venta = new Venta(
                        rs.getInt("venta_id"),
                        rs.getInt("cliente_id"),
                        rs.getDate("fecha_venta"),
                        rs.getDouble("total")
                );
                venta.setDetalles(obtenerDetallesVenta(venta.getVentaId()));
                ventas.add(venta);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas por cliente: " + e.getMessage());
        }
        return ventas;
    }

    @Override
    public boolean actualizarVenta(Venta venta) {
        String sql = "UPDATE Ventas SET cliente_id = ?, fecha_venta = ?, total = ? WHERE venta_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, venta.getClienteId());
            stmt.setDate(2, venta.getFechaVenta());
            stmt.setDouble(3, venta.getTotal());
            stmt.setInt(4, venta.getVentaId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar venta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarVenta(int id) {
        // Primero eliminamos los detalles de la venta
        String sqlDetalles = "DELETE FROM Detalles_Ventas WHERE venta_id = ?";
        String sqlVenta = "DELETE FROM Ventas WHERE venta_id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmtDetalles = connection.prepareStatement(sqlDetalles);
                 PreparedStatement stmtVenta = connection.prepareStatement(sqlVenta)) {

                stmtDetalles.setInt(1, id);
                stmtDetalles.executeUpdate();

                stmtVenta.setInt(1, id);
                int resultado = stmtVenta.executeUpdate();

                connection.commit();
                return resultado > 0;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar venta: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restaurar autocommit: " + e.getMessage());
            }
        }
    }

    @Override
    public double calcularTotalVentasPorCliente(int clienteId) {
        String sql = "SELECT SUM(total) as total FROM Ventas WHERE cliente_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular total de ventas por cliente: " + e.getMessage());
        }
        return 0.0;
    }

    @Override
    public List<Venta> obtenerVentasDelDia() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas WHERE DATE(fecha_venta) = CURRENT_DATE";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Venta venta = new Venta(
                        rs.getInt("cliente_id"),
                        rs.getDate("fecha_venta"),
                        rs.getDouble("total")
                );
                venta.setVentaId(rs.getInt("venta_id"));
                venta.setDetalles(obtenerDetallesVenta(venta.getVentaId()));
                ventas.add(venta);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las ventas del día: " + e.getMessage());
        }
        return ventas;
    }

    @Override
    public List<Object[]> obtenerProductosMasVendidos() {
        List<Object[]> productos = new ArrayList<>();
        String sql = "SELECT p.nombre, SUM(dv.cantidad) as cantidad_total, SUM(dv.subtotal) as total " +
                "FROM detalles_ventas dv " +
                "JOIN productos p ON dv.producto_id = p.producto_id " +
                "GROUP BY p.producto_id, p.nombre " +
                "ORDER BY cantidad_total DESC " +
                "LIMIT 10";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] producto = new Object[]{
                        rs.getString("nombre"),
                        rs.getInt("cantidad_total"),
                        rs.getDouble("total")
                };
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los productos más vendidos: " + e.getMessage());
        }
        return productos;
    }

    @Override
    public List<Object[]> obtenerClientesFrecuentes() {
        List<Object[]> clientes = new ArrayList<>();
        String sql = "SELECT c.nombre, COUNT(v.venta_id) as total_compras, SUM(v.total) as total_gastado " +
                "FROM clientes c " +
                "JOIN ventas v ON c.cliente_id = v.cliente_id " +
                "GROUP BY c.cliente_id, c.nombre " +
                "ORDER BY total_compras DESC " +
                "LIMIT 10";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] cliente = new Object[]{
                        rs.getString("nombre"),
                        rs.getInt("total_compras"),
                        rs.getDouble("total_gastado")
                };
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los clientes frecuentes: " + e.getMessage());
        }
        return clientes;
    }
}