package models;

public class DetalleVenta {
    private int detalleId;
    private int ventaId;
    private int productoId;
    private int cantidad;
    private double subtotal;

    // Constructor sin ID (para nuevos detalles)
    public DetalleVenta(int ventaId, int productoId, int cantidad, double subtotal) {
        this.ventaId = ventaId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    // Constructor con ID (para detalles existentes)
    public DetalleVenta(int detalleId, int ventaId, int productoId, int cantidad, double subtotal) {
        this.detalleId = detalleId;
        this.ventaId = ventaId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    // Getters y Setters
    public int getDetalleId() {
        return detalleId;
    }

    public void setDetalleId(int detalleId) {
        this.detalleId = detalleId;
    }

    public int getVentaId() {
        return ventaId;
    }

    public void setVentaId(int ventaId) {
        this.ventaId = ventaId;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
} 