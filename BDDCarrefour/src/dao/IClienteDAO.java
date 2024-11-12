package dao;

import java.util.List;
import models.Cliente;

public interface IClienteDAO {
    boolean crearCliente(Cliente cliente);
    Cliente obtenerClientePorId(int id);
    List<Cliente> obtenerTodosLosClientes();
    boolean actualizarCliente(Cliente cliente);
    boolean eliminarCliente(int id);
} 