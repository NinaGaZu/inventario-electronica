package inventario.modelo;

import java.util.Date;

public class Venta {
    private int idVenta;
    private Producto producto;
    private Cliente cliente;
    private int cantidad;
    private double precioUnitario;
    private double total;
    private String tipoCompra; // "linea" o "tienda"
    private Date fecha;

    public Venta() {
        this.fecha = new Date();
    }

    public Venta(int idVenta, Producto producto, Cliente cliente, int cantidad, 
                 double precioUnitario, String tipoCompra) {
        this.idVenta = idVenta;
        this.producto = producto;
        this.cliente = cliente;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.tipoCompra = tipoCompra;
        this.fecha = new Date();
        calcularTotal();
    }

    private void calcularTotal() {
        double subtotal = cantidad * precioUnitario;
        
        // Aplicar 15% de descuento si es compra en línea y mayor a 3 productos
        if (tipoCompra != null && tipoCompra.equalsIgnoreCase("linea") && cantidad > 3) {
            double descuento = subtotal * 0.15;
            this.total = subtotal - descuento;
        } else {
            this.total = subtotal;
        }
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularTotal();
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        calcularTotal();
    }

    public double getTotal() {
        return total;
    }

    public String getTipoCompra() {
        return tipoCompra;
    }

    public void setTipoCompra(String tipoCompra) {
        this.tipoCompra = tipoCompra;
        calcularTotal();
    }

    public Date getFecha() {
        return fecha;
    }

    public double getDescuentoAplicado() {
        if (tipoCompra.equalsIgnoreCase("linea") && cantidad > 3) {
            return (cantidad * precioUnitario) * 0.15;
        }
        return 0;
    }
}