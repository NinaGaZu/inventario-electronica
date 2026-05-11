package inventario.dao;

import inventario.modelo.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para gestionar las operaciones de base de datos del inventario
 * @author Gianina Gaete
 * @version 1.0
 */
public class InventarioDAO {
    
    // Atributos de conexión JDBC
    private static final String URL = "jdbc:mysql://localhost:3306/inventario_electronica";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "FamGaZu05";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private Connection conexion;
    
    /**
     * Constructor - Establece la conexión a la base de datos
     */
    public InventarioDAO() {
        try {
            // Paso 1: Cargar el controlador JDBC
            Class.forName(DRIVER);
            
            // Paso 2: Establecer la conexión
            this.conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            
            if (this.conexion != null) {
                System.out.println("✓ Conexión exitosa a la base de datos Inventario Electrónica");
            }
            
        } catch (ClassNotFoundException e) {
            System.out.println("✗ Error: Controlador JDBC MySQL no encontrado");
            System.out.println("Detalle: " + e.getMessage());
            System.out.println("Solución: Agregar mysql-connector-java.jar al proyecto");
        } catch (SQLException e) {
            System.out.println("✗ Error: No se pudo establecer la conexión");
            System.out.println("Detalle: " + e.getMessage());
            System.out.println("Verificar: MySQL está ejecutándose y las credenciales son correctas");
        }
    }
    
    // ==================== OPERACIONES CON PRODUCTOS ====================
    
    /**
     * INSERT - Registrar nuevo producto
     * @param producto Objeto Producto a registrar
     * @return boolean true si fue exitoso
     */
    public boolean agregarProducto(Producto producto) {
        String sql = "INSERT INTO productos (codigo, descripcion, cantidad, precio, ubicacion) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try {
            // Paso 3: Crear PreparedStatement
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            
            // Asignar parámetros
            pstmt.setString(1, producto.getCodigo());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setInt(3, producto.getCantidad());
            pstmt.setDouble(4, producto.getPrecio());
            pstmt.setString(5, producto.getUbicacion());
            
            // Ejecutar inserción
            int filasAfectadas = pstmt.executeUpdate();
            
            // Cerrar recursos - Paso 5
            pstmt.close();
            
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("✗ Error al registrar producto: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * SELECT - Obtener todos los productos
     * @return List<Producto> lista de productos
     */
    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY descripcion";
        
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getString("codigo"),
                    rs.getString("descripcion"),
                    rs.getInt("cantidad"),
                    rs.getDouble("precio"),
                    rs.getString("ubicacion")
                );
                productos.add(producto);
            }
            
        } catch (SQLException e) {
            System.out.println("✗ Error al obtener productos: " + e.getMessage());
        }
        
        return productos;
    }
    
    /**
     * SELECT - Obtener producto por código
     * @param codigo Código del producto
     * @return Producto encontrado o null
     */
    public Producto obtenerProductoPorCodigo(String codigo) {
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        
        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, codigo);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Producto producto = new Producto(
                    rs.getString("codigo"),
                    rs.getString("descripcion"),
                    rs.getInt("cantidad"),
                    rs.getDouble("precio"),
                    rs.getString("ubicacion")
                );
                rs.close();
                pstmt.close();
                return producto;
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            System.out.println("✗ Error al buscar producto: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * UPDATE - Actualizar producto
     * @param codigo Código del producto a actualizar
     * @param productoActualizado Producto con los nuevos datos
     * @return boolean true si fue exitoso
     */
    public boolean actualizarProducto(String codigo, Producto productoActualizado) {
        String sql = "UPDATE productos SET descripcion = ?, cantidad = ?, precio = ?, ubicacion = ? " +
                    "WHERE codigo = ?";
        
        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            
            pstmt.setString(1, productoActualizado.getDescripcion());
            pstmt.setInt(2, productoActualizado.getCantidad());
            pstmt.setDouble(3, productoActualizado.getPrecio());
            pstmt.setString(4, productoActualizado.getUbicacion());
            pstmt.setString(5, codigo);
            
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("✗ Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * UPDATE - Actualizar stock después de una venta
     * @param codigo Código del producto
     * @param cantidadVendida Cantidad vendida
     * @return boolean true si fue exitoso
     */
    public boolean actualizarStock(String codigo, int cantidadVendida) {
        String sql = "UPDATE productos SET cantidad = cantidad - ? WHERE codigo = ? AND cantidad >= ?";
        
        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, cantidadVendida);
            pstmt.setString(2, codigo);
            pstmt.setInt(3, cantidadVendida);
            
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("✗ Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * DELETE - Eliminar producto
     * @param codigo Código del producto a eliminar
     * @return boolean true si fue exitoso
     */
    public boolean eliminarProducto(String codigo) {
        String sql = "DELETE FROM productos WHERE codigo = ?";
        
        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, codigo);
            
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("✗ Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== OPERACIONES CON CLIENTES ====================
    
    /**
     * INSERT - Registrar nuevo cliente
     * @param cliente Objeto Cliente a registrar
     * @return boolean true si fue exitoso
     */
    public boolean agregarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, direccion, correo, telefono) " +
                    "VALUES (?, ?, ?, ?)";
        
        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getDireccion());
            pstmt.setString(3, cliente.getCorreo());
            pstmt.setString(4, cliente.getTelefono());
            
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("✗ Error al registrar cliente: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * SELECT - Obtener todos los clientes
     * @return List<Cliente> lista de clientes
     */
    public List<Cliente> obtenerTodosLosClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nombre";
        
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getString("correo"),
                    rs.getString("telefono")
                );
                clientes.add(cliente);
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.out.println("✗ Error al obtener clientes: " + e.getMessage());
        }
        
        return clientes;
    }
    
    // ==================== OPERACIONES CON PROVEEDORES ====================
    
    /**
     * INSERT - Registrar nuevo proveedor
     * @param proveedor Objeto Proveedor a registrar
     * @return boolean true si fue exitoso
     */
    public boolean agregarProveedor(Proveedor proveedor) {
        String sql = "INSERT INTO proveedores (nombre, direccion, correo, telefono) " +
                    "VALUES (?, ?, ?, ?)";
        
        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            
            pstmt.setString(1, proveedor.getNombre());
            pstmt.setString(2, proveedor.getDireccion());
            pstmt.setString(3, proveedor.getCorreo());
            pstmt.setString(4, proveedor.getTelefono());
            
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("✗ Error al registrar proveedor: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * SELECT - Obtener todos los proveedores
     * @return List<Proveedor> lista de proveedores
     */
    public List<Proveedor> obtenerTodosLosProveedores() {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM proveedores ORDER BY nombre";
        
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Proveedor proveedor = new Proveedor(
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getString("correo"),
                    rs.getString("telefono")
                );
                proveedores.add(proveedor);
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.out.println("✗ Error al obtener proveedores: " + e.getMessage());
        }
        
        return proveedores;
    }
    
    // ==================== OPERACIONES CON VENTAS ====================
    
    /**
     * Registrar venta con parámetros individuales
     * @param producto Producto vendido
     * @param cliente Cliente que compra
     * @param cantidad Cantidad vendida
     * @param tipoCompra Tipo de compra (linea/tienda)
     * @return Venta objeto Venta creado o null si falla
     */
    public Venta registrarVenta(Producto producto, Cliente cliente, 
                               int cantidad, String tipoCompra) {
        
        // Validar stock
        if (producto.getCantidad() < cantidad) {
            System.out.println("✗ Stock insuficiente");
            return null;
        }
        
        // Calcular total con descuento si aplica
        double precioUnitario = producto.getPrecio();
        double subtotal = precioUnitario * cantidad;
        double descuento = 0;
        double total = subtotal;
        
        // Aplicar 15% descuento si es compra en línea y más de 3 productos
        if (tipoCompra.equalsIgnoreCase("linea") && cantidad > 3) {
            descuento = subtotal * 0.15;
            total = subtotal - descuento;
        }
        
        // Crear objeto Venta
        Venta venta = new Venta(0, producto, cliente, cantidad, precioUnitario, tipoCompra);
        
        // Insertar en base de datos
        String sql = "INSERT INTO ventas (producto_codigo, cliente_nombre, cantidad, " +
                    "precio_unitario, total, tipo_compra, fecha) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            
            pstmt.setString(1, producto.getCodigo());
            pstmt.setString(2, cliente.getNombre());
            pstmt.setInt(3, cantidad);
            pstmt.setDouble(4, precioUnitario);
            pstmt.setDouble(5, total);
            pstmt.setString(6, tipoCompra);
            pstmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            
            if (filasAfectadas > 0) {
                // Actualizar stock del producto
                producto.setCantidad(producto.getCantidad() - cantidad);
                actualizarStockProducto(producto);
                return venta;
            }
            
        } catch (SQLException e) {
            System.out.println("✗ Error al registrar venta: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Método auxiliar para actualizar stock
     */
    private void actualizarStockProducto(Producto producto) {
        String sql = "UPDATE productos SET cantidad = ? WHERE codigo = ?";
        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, producto.getCantidad());
            pstmt.setString(2, producto.getCodigo());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("✗ Error al actualizar stock: " + e.getMessage());
        }
    }
    
    /**
     * SELECT - Obtener todas las ventas
     * @return List<Venta> lista de ventas
     */
    public List<Venta> obtenerTodasLasVentas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas ORDER BY fecha DESC";
        
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                // Crear objetos temporales (en una implementación real, cargarías desde BD)
                Producto producto = new Producto(
                    rs.getString("producto_codigo"),
                    "", 0, 0, ""
                );
                
                Cliente cliente = new Cliente(
                    rs.getString("cliente_nombre"),
                    "", "", ""
                );
                
                Venta venta = new Venta(
                    rs.getInt("id"),
                    producto,
                    cliente,
                    rs.getInt("cantidad"),
                    rs.getDouble("precio_unitario"),
                    rs.getString("tipo_compra")
                );
                
                ventas.add(venta);
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.out.println("✗ Error al obtener ventas: " + e.getMessage());
        }
        
        return ventas;
    }
    
    /**
     * SELECT - Obtener ventas del día
     * @return List<Venta> ventas del día actual
     */
    public List<Venta> obtenerVentasDelDia() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas WHERE DATE(fecha) = CURDATE() ORDER BY fecha DESC";
        
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getString("producto_codigo"),
                    "", 0, 0, ""
                );
                
                Cliente cliente = new Cliente(
                    rs.getString("cliente_nombre"),
                    "", "", ""
                );
                
                Venta venta = new Venta(
                    rs.getInt("id"),
                    producto,
                    cliente,
                    rs.getInt("cantidad"),
                    rs.getDouble("precio_unitario"),
                    rs.getString("tipo_compra")
                );
                
                ventas.add(venta);
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.out.println("✗ Error al obtener ventas del día: " + e.getMessage());
        }
        
        return ventas;
    }
    
    // ==================== MÉTODOS ADICIONALES PARA REPORTES ====================
    
    /**
     * Obtener valor total del inventario
     * @return double valor total
     */
    public double obtenerValorTotalInventario() {
        double total = 0;
        String sql = "SELECT SUM(cantidad * precio) as valor_total FROM productos";
        
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                total = rs.getDouble("valor_total");
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.out.println("✗ Error al calcular valor del inventario: " + e.getMessage());
        }
        
        return total;
    }
    
    /**
     * Obtener productos con stock bajo
     * @param limite Límite de stock mínimo
     * @return List<Producto> productos con stock bajo
     */
    public List<Producto> obtenerProductosBajoStock(int limite) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE cantidad <= ? ORDER BY cantidad ASC";
        
        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, limite);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getString("codigo"),
                    rs.getString("descripcion"),
                    rs.getInt("cantidad"),
                    rs.getDouble("precio"),
                    rs.getString("ubicacion")
                );
                productos.add(producto);
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            System.out.println("✗ Error al obtener productos con stock bajo: " + e.getMessage());
        }
        
        return productos;
    }
    
    /**
     * Cerrar la conexión a la base de datos
     */
    public void cerrarConexion() {
        try {
            if (this.conexion != null && !this.conexion.isClosed()) {
                this.conexion.close();
                System.out.println("✓ Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.out.println("✗ Error al cerrar la conexión: " + e.getMessage());
        }
    }
    
    /**
     * Método público para exponer la conexión JDBC
     * Necesario para integración con JasperReports
     * @return Connection objeto de conexión activo
     */
    public Connection obtenerConexion() {
        return this.conexion;
    }
}