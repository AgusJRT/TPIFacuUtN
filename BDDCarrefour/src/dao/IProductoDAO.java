package dao;

import java.util.List;
import models.Producto;

public interface IProductoDAO {
    boolean crearProducto(Producto producto);
    Producto obtenerProductoPorId(int id);
    List<Producto> obtenerTodosLosProductos();
    boolean actualizarProducto(Producto producto);
    boolean eliminarProducto(int id);
    List<Producto> buscarProductosPorNombre(String nombre);
    boolean actualizarStock(int productoId, int nuevoStock);
} 