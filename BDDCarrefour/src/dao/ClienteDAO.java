package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Cliente;
import config.DatabaseConnection;

public class ClienteDAO implements IClienteDAO {
    private Connection connection;

    public ClienteDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public boolean crearCliente(Cliente cliente) {
        String sql = "INSERT INTO Clientes (nombre, email, telefono) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefono());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Cliente obtenerClientePorId(int id) {
        String sql = "SELECT * FROM Clientes WHERE cliente_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Cliente(
                    rs.getInt("cliente_id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("telefono")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cliente: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Cliente> obtenerTodosLosClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("cliente_id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("telefono")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener clientes: " + e.getMessage());
        }
        return clientes;
    }

    @Override
    public boolean actualizarCliente(Cliente cliente) {
        String sql = "UPDATE Clientes SET nombre = ?, email = ?, telefono = ? WHERE cliente_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefono());
            stmt.setInt(4, cliente.getClienteId());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarCliente(int id) {
        String sql = "DELETE FROM Clientes WHERE cliente_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
} 