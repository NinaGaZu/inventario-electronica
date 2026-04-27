package inventario.dao;

import inventario.modelo.*;
import java.util.*;

public class InventarioDAO {
    private List<Producto> productos;
    private List<Cliente> clientes;
    private List<Proveedor> proveedores;
    private List<Venta> ventas;
    private int contadorVentas;

    public InventarioDAO() {
        this.productos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.proveedores = new ArrayList<>();
        this.ventas = new ArrayList<>();
        this.contadorVentas = 0;
        
        // Datos iniciales de prueba
        inicializarDatos();
    }

    private void inicializarDatos() {
        productos.add(new Producto("P001", "Laptop Dell Inspiron", 10, 428000, "Almacén A1"));
        productos.add(new Producto("P002", "Mouse Logitech", 50, 14990, "Almacén B2"));
        productos.add(new Producto("P003", "Teclado Mecánico", 30, 72000, "Almacén B3"));
        productos.add(new Producto("P004", "Monitor Samsung 24\"", 15, 105990, "Almacén C1"));
        
        clientes.add(new Cliente("Juan Pérez", "Av. Principal 123", "juan123@gmail.com", "987654321"));
        clientes.add(new Cliente("María González", "Calle Secundaria 456", "maria456@gmail.com", "912345678"));
        
        proveedores.add(new Proveedor("TechSupply SA", "Av. Industrial 789", "contacto@techsupply.com", "223344556"));
    }

    // Operaciones GET - Lectura
    public List<Producto> obtenerTodosLosProductos() {
        return productos;
    }

    public Producto obtenerProductoPorCodigo(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }

    public List<Cliente> obtenerTodosLosClientes() {
        return clientes;
    }

    public List<Proveedor> obtenerTodosLosProveedores() {
        return proveedores;
    }

    public List<Venta> obtenerTodasLasVentas() {
        return ventas;
    }

    // Operaciones POST - Creación
    public boolean agregarProducto(Producto producto) {
        if (obtenerProductoPorCodigo(producto.getCodigo()) == null) {
            productos.add(producto);
            return true;
        }
        return false;
    }

    public boolean agregarCliente(Cliente cliente) {
        clientes.add(cliente);
        return true;
    }

    public boolean agregarProveedor(Proveedor proveedor) {
        proveedores.add(proveedor);
        return true;
    }

    public Venta registrarVenta(Producto producto, Cliente cliente,int cantidad, String tipoCompra) {
        // Validar disponibilidad de stock
        if (producto.getCantidad() >= cantidad) {
            // Actualizar inventario
            producto.setCantidad(producto.getCantidad() - cantidad);
            
            // Crear venta
            contadorVentas++;
            Venta venta = new Venta(contadorVentas, producto, cliente, cantidad, 
                                   producto.getPrecio(), tipoCompra);
            ventas.add(venta);
            return venta;
        }
        return null;
    }

    // Operaciones PUT - Actualización
    public boolean actualizarProducto(String codigo, Producto productoActualizado) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo().equalsIgnoreCase(codigo)) {
                productos.set(i, productoActualizado);
                return true;
            }
        }
        return false;
    }

    public boolean actualizarStock(String codigo, int nuevaCantidad) {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                p.setCantidad(nuevaCantidad);
                return true;
            }
        }
        return false;
    }

    // Operaciones DELETE - Eliminación
    public boolean eliminarProducto(String codigo) {
        Producto producto = obtenerProductoPorCodigo(codigo);
        if (producto != null) {
            productos.remove(producto);
            return true;
        }
        return false;
    }

    // Métodos adicionales para reportes
    public double obtenerValorTotalInventario() {
        double total = 0;
        for (Producto p : productos) {
            total += p.getPrecio() * p.getCantidad();
        }
        return total;
    }

    public List<Producto> obtenerProductosBajoStock(int limite) {
        List<Producto> bajoStock = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getCantidad() <= limite) {
                bajoStock.add(p);
            }
        }
        return bajoStock;
    }
}