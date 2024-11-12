package models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private int ventaId;
    private int clienteId;
    private Date fechaVenta;
    private double total;
    private List<DetalleVenta> detalles;

    // Constructor con ID
    public Venta(int ventaId, int clienteId, Date fechaVenta, double total) {
        this.ventaId = ventaId;
        this.clienteId = clienteId;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.detalles = new ArrayList<>();
    }

    // Constructor sin ID
    public Venta(int clienteId, Date fechaVenta, double total) {
        this.clienteId = clienteId;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.detalles = new ArrayList<>();
    }

    // Getters y Setters
    public int getVentaId() {
        return ventaId;
    }

    public void setVentaId(int ventaId) {
        this.ventaId = ventaId;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    public void addDetalle(DetalleVenta detalle) {
        this.detalles.add(detalle);
    }
} 