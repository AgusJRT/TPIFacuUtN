package dao;

import java.util.List;
import models.Venta;
import models.DetalleVenta;

public interface IVentaDAO {
    int crearVenta(Venta venta);
    boolean agregarDetalleVenta(DetalleVenta detalle);
    Venta obtenerVentaPorId(int id);
    List<Venta> obtenerTodasLasVentas();
    List<Venta> obtenerVentasPorCliente(int clienteId);
    List<DetalleVenta> obtenerDetallesVenta(int ventaId);
    boolean actualizarVenta(Venta venta);
    boolean eliminarVenta(int id);
    double calcularTotalVentasPorCliente(int clienteId);
    List<Venta> obtenerVentasDelDia();
    List<Object[]> obtenerProductosMasVendidos();
    List<Object[]> obtenerClientesFrecuentes();
}