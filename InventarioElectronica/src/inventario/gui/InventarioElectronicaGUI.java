package inventario.gui;

import inventario.dao.InventarioDAO;
import inventario.modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class InventarioElectronicaGUI extends JFrame {
    private InventarioDAO inventarioDAO;
    private JTabbedPane tabbedPane;
    
    // Componentes para Productos
    private JTextField txtCodigoProd, txtDescProd, txtCantidadProd, txtPrecioProd, txtUbicacionProd;
    private JButton btnRegistrarProd, btnActualizarProd, btnEliminarProd, btnConsultarProd;
    private JTable tablaProductos;
    private DefaultTableModel modeloTablaProductos;
    
    // Componentes para Clientes
    private JTextField txtNombreCli, txtDireccionCli, txtCorreoCli, txtTelefonoCli;
    private JButton btnRegistrarCli;
    private JTable tablaClientes;
    private DefaultTableModel modeloTablaClientes;
    
    // Componentes para Proveedores
    private JTextField txtNombreProv, txtDireccionProv, txtCorreoProv, txtTelefonoProv;
    private JButton btnRegistrarProv;
    private JTable tablaProveedores;
    private DefaultTableModel modeloTablaProveedores;
    
    // Componentes para Ventas
    private JComboBox<String> comboProductos, comboClientes;
    private JTextField txtCantidadVenta;
    private JRadioButton radioLinea, radioTienda;
    private JButton btnRegistrarVenta;
    private JTextArea txtReporteVenta;
    private JTable tablaVentas;
    private DefaultTableModel modeloTablaVentas;
    
    // Componentes para Reportes
    private JTextArea txtReporteInventario;
    private JButton btnGenerarReporte;

    public InventarioElectronicaGUI() {
        inventarioDAO = new InventarioDAO();
        initComponents();
        configurarVentana();
        cargarDatosIniciales();
    }

    private void initComponents() {
        // Crear panel con pestañas
        tabbedPane = new JTabbedPane();
        
        // Inicializar componentes de cada pestaña
        initPanelProductos();
        initPanelClientes();
        initPanelProveedores();
        initPanelVentas();
        initPanelReportes();
        
        // Agregar pestañas al tabbedPane
        tabbedPane.addTab("Productos", panelProductos);
        tabbedPane.addTab("Clientes", panelClientes);
        tabbedPane.addTab("Proveedores", panelProveedores);
        tabbedPane.addTab("Ventas", panelVentas);
        tabbedPane.addTab("Reportes", panelReportes);
        
        // Agregar tabbedPane a la ventana
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel panelProductos;
    private void initPanelProductos() {
        panelProductos = new JPanel(new BorderLayout());
        
        // Panel superior - Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registro de Productos"));
        
        panelFormulario.add(new JLabel("Código:"));
        txtCodigoProd = new JTextField();
        panelFormulario.add(txtCodigoProd);
        
        panelFormulario.add(new JLabel("Descripción:"));
        txtDescProd = new JTextField();
        panelFormulario.add(txtDescProd);
        
        panelFormulario.add(new JLabel("Cantidad:"));
        txtCantidadProd = new JTextField();
        panelFormulario.add(txtCantidadProd);
        
        panelFormulario.add(new JLabel("Precio:"));
        txtPrecioProd = new JTextField();
        panelFormulario.add(txtPrecioProd);
        
        panelFormulario.add(new JLabel("Ubicación:"));
        txtUbicacionProd = new JTextField();
        panelFormulario.add(txtUbicacionProd);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnRegistrarProd = new JButton("Registrar");
        btnActualizarProd = new JButton("Actualizar");
        btnEliminarProd = new JButton("Eliminar");
        btnConsultarProd = new JButton("Consultar");
        
        panelBotones.add(btnRegistrarProd);
        panelBotones.add(btnActualizarProd);
        panelBotones.add(btnEliminarProd);
        panelBotones.add(btnConsultarProd);
        
        panelFormulario.add(panelBotones);
        
        // Tabla de productos
        String[] columnasProd = {"Código", "Descripción", "Cantidad", "Precio", "Ubicación"};
        modeloTablaProductos = new DefaultTableModel(columnasProd, 0);
        tablaProductos = new JTable(modeloTablaProductos);
        JScrollPane scrollTablaProd = new JScrollPane(tablaProductos);
        
        panelProductos.add(panelFormulario, BorderLayout.NORTH);
        panelProductos.add(scrollTablaProd, BorderLayout.CENTER);
        
        // Eventos de botones
        btnRegistrarProd.addActionListener(e -> registrarProducto());
        btnActualizarProd.addActionListener(e -> actualizarProducto());
        btnEliminarProd.addActionListener(e -> eliminarProducto());
        btnConsultarProd.addActionListener(e -> cargarProductosEnTabla());
    }

    private JPanel panelClientes;
    private void initPanelClientes() {
        panelClientes = new JPanel(new BorderLayout());
        
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registro de Clientes"));
        
        panelFormulario.add(new JLabel("Nombre:"));
        txtNombreCli = new JTextField();
        panelFormulario.add(txtNombreCli);
        
        panelFormulario.add(new JLabel("Dirección:"));
        txtDireccionCli = new JTextField();
        panelFormulario.add(txtDireccionCli);
        
        panelFormulario.add(new JLabel("Correo:"));
        txtCorreoCli = new JTextField();
        panelFormulario.add(txtCorreoCli);
        
        panelFormulario.add(new JLabel("Teléfono:"));
        txtTelefonoCli = new JTextField();
        panelFormulario.add(txtTelefonoCli);
        
        btnRegistrarCli = new JButton("Registrar Cliente");
        panelFormulario.add(btnRegistrarCli);
        
        String[] columnasCli = {"Nombre", "Dirección", "Correo", "Teléfono"};
        modeloTablaClientes = new DefaultTableModel(columnasCli, 0);
        tablaClientes = new JTable(modeloTablaClientes);
        JScrollPane scrollTablaCli = new JScrollPane(tablaClientes);
        
        panelClientes.add(panelFormulario, BorderLayout.NORTH);
        panelClientes.add(scrollTablaCli, BorderLayout.CENTER);
        
        btnRegistrarCli.addActionListener(e -> registrarCliente());
    }

    private JPanel panelProveedores;
    private void initPanelProveedores() {
        panelProveedores = new JPanel(new BorderLayout());
        
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registro de Proveedores"));
        
        panelFormulario.add(new JLabel("Nombre:"));
        txtNombreProv = new JTextField();
        panelFormulario.add(txtNombreProv);
        
        panelFormulario.add(new JLabel("Dirección:"));
        txtDireccionProv = new JTextField();
        panelFormulario.add(txtDireccionProv);
        
        panelFormulario.add(new JLabel("Correo:"));
        txtCorreoProv = new JTextField();
        panelFormulario.add(txtCorreoProv);
        
        panelFormulario.add(new JLabel("Teléfono:"));
        txtTelefonoProv = new JTextField();
        panelFormulario.add(txtTelefonoProv);
        
        JButton btnRegistrarProv = new JButton("Registrar Proveedor");
        panelFormulario.add(btnRegistrarProv);
        
        String[] columnasProv = {"Nombre", "Dirección", "Correo", "Teléfono"};
        modeloTablaProveedores = new DefaultTableModel(columnasProv, 0);
        tablaProveedores = new JTable(modeloTablaProveedores);
        JScrollPane scrollTablaProv = new JScrollPane(tablaProveedores);
        
        panelProveedores.add(panelFormulario, BorderLayout.NORTH);
        panelProveedores.add(scrollTablaProv, BorderLayout.CENTER);
        
        btnRegistrarProv.addActionListener(e -> registrarProveedor());
    }

    private JPanel panelVentas;
    private void initPanelVentas() {
        panelVentas = new JPanel(new BorderLayout());
        
        JPanel panelSuperior = new JPanel(new GridLayout(6, 2, 5, 5));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Registro de Ventas"));
        
        panelSuperior.add(new JLabel("Producto:"));
        comboProductos = new JComboBox<>();
        panelSuperior.add(comboProductos);
        
        panelSuperior.add(new JLabel("Cliente:"));
        comboClientes = new JComboBox<>();
        panelSuperior.add(comboClientes);
        
        panelSuperior.add(new JLabel("Cantidad:"));
        txtCantidadVenta = new JTextField();
        panelSuperior.add(txtCantidadVenta);
        
        panelSuperior.add(new JLabel("Tipo de Compra:"));
        JPanel panelTipoCompra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioLinea = new JRadioButton("En Línea");
        radioTienda = new JRadioButton("Tienda Física");
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(radioLinea);
        grupoTipo.add(radioTienda);
        radioTienda.setSelected(true);
        panelTipoCompra.add(radioLinea);
        panelTipoCompra.add(radioTienda);
        panelSuperior.add(panelTipoCompra);
        
        btnRegistrarVenta = new JButton("Registrar Venta");
        panelSuperior.add(btnRegistrarVenta);
        
        txtReporteVenta = new JTextArea(5, 30);
        txtReporteVenta.setEditable(false);
        txtReporteVenta.setBorder(BorderFactory.createTitledBorder("Detalle de Venta"));
        panelSuperior.add(new JScrollPane(txtReporteVenta));
        
        String[] columnasVentas = {"ID", "Producto", "Cliente", "Cantidad", "Total", "Tipo", "Fecha"};
        modeloTablaVentas = new DefaultTableModel(columnasVentas, 0);
        tablaVentas = new JTable(modeloTablaVentas);
        JScrollPane scrollTablaVentas = new JScrollPane(tablaVentas);
        
        panelVentas.add(panelSuperior, BorderLayout.NORTH);
        panelVentas.add(scrollTablaVentas, BorderLayout.CENTER);
        
        btnRegistrarVenta.addActionListener(e -> registrarVenta());
    }

    private JPanel panelReportes;
    private void initPanelReportes() {
        panelReportes = new JPanel(new BorderLayout());
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnGenerarReporte = new JButton("Generar Reporte de Inventario");
        panelBotones.add(btnGenerarReporte);
        
        txtReporteInventario = new JTextArea();
        txtReporteInventario.setEditable(false);
        JScrollPane scrollReporte = new JScrollPane(txtReporteInventario);
        
        panelReportes.add(panelBotones, BorderLayout.NORTH);
        panelReportes.add(scrollReporte, BorderLayout.CENTER);
        
        btnGenerarReporte.addActionListener(e -> generarReporteInventario());
    }

    private void configurarVentana() {
        setTitle("Sistema de Inventario - Tienda de Electrónicos");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Evento de cierre de ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirmacion = JOptionPane.showConfirmDialog(
                    InventarioElectronicaGUI.this,
                    "¿Desea salir del sistema?",
                    "Confirmar Salida",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirmacion != JOptionPane.YES_OPTION) {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }

    private void cargarDatosIniciales() {
        cargarProductosEnTabla();
        cargarClientesEnTabla();
        cargarProveedoresEnTabla();
        actualizarComboProductos();
        actualizarComboClientes();
    }

    // Métodos para Productos
    private void registrarProducto() {
        try {
            String codigo = txtCodigoProd.getText().trim();
            String descripcion = txtDescProd.getText().trim();
            int cantidad = Integer.parseInt(txtCantidadProd.getText().trim());
            double precio = Double.parseDouble(txtPrecioProd.getText().trim());
            String ubicacion = txtUbicacionProd.getText().trim();
            
            if (codigo.isEmpty() || descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Complete todos los campos obligatorios", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Producto producto = new Producto(codigo, descripcion, cantidad, precio, ubicacion);
            
            if (inventarioDAO.agregarProducto(producto)) {
                JOptionPane.showMessageDialog(this, 
                    "Producto registrado exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormularioProducto();
                cargarProductosEnTabla();
                actualizarComboProductos();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "El código de producto ya existe", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Cantidad y precio deben ser valores numéricos", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarProducto() {
        try {
            String codigo = txtCodigoProd.getText().trim();
            Producto existente = inventarioDAO.obtenerProductoPorCodigo(codigo);
            
            if (existente == null) {
                JOptionPane.showMessageDialog(this, 
                    "Producto no encontrado", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String descripcion = txtDescProd.getText().trim();
            int cantidad = Integer.parseInt(txtCantidadProd.getText().trim());
            double precio = Double.parseDouble(txtPrecioProd.getText().trim());
            String ubicacion = txtUbicacionProd.getText().trim();
            
            Producto actualizado = new Producto(codigo, descripcion, cantidad, precio, ubicacion);
            
            if (inventarioDAO.actualizarProducto(codigo, actualizado)) {
                JOptionPane.showMessageDialog(this, 
                    "Producto actualizado exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarProductosEnTabla();
                actualizarComboProductos();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Cantidad y precio deben ser valores numéricos", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto() {
        String codigo = txtCodigoProd.getText().trim();
        
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Ingrese el código del producto a eliminar", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar el producto?", 
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (inventarioDAO.eliminarProducto(codigo)) {
                JOptionPane.showMessageDialog(this, 
                    "Producto eliminado exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormularioProducto();
                cargarProductosEnTabla();
                actualizarComboProductos();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Producto no encontrado", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarProductosEnTabla() {
        modeloTablaProductos.setRowCount(0);
        List<Producto> productos = inventarioDAO.obtenerTodosLosProductos();
        
        for (Producto p : productos) {
            Object[] fila = {
                p.getCodigo(),
                p.getDescripcion(),
                p.getCantidad(),
                String.format("$%.2f", p.getPrecio()),
                p.getUbicacion()
            };
            modeloTablaProductos.addRow(fila);
        }
    }

    private void limpiarFormularioProducto() {
        txtCodigoProd.setText("");
        txtDescProd.setText("");
        txtCantidadProd.setText("");
        txtPrecioProd.setText("");
        txtUbicacionProd.setText("");
    }

    // Métodos para Clientes
    private void registrarCliente() {
        String nombre = txtNombreCli.getText().trim();
        String direccion = txtDireccionCli.getText().trim();
        String correo = txtCorreoCli.getText().trim();
        String telefono = txtTelefonoCli.getText().trim();
        
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El nombre es obligatorio", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Cliente cliente = new Cliente(nombre, direccion, correo, telefono);
        inventarioDAO.agregarCliente(cliente);
        
        JOptionPane.showMessageDialog(this, 
            "Cliente registrado exitosamente", 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
        
        txtNombreCli.setText("");
        txtDireccionCli.setText("");
        txtCorreoCli.setText("");
        txtTelefonoCli.setText("");
        
        cargarClientesEnTabla();
        actualizarComboClientes();
    }

    private void cargarClientesEnTabla() {
        modeloTablaClientes.setRowCount(0);
        List<Cliente> clientes = inventarioDAO.obtenerTodosLosClientes();
        
        for (Cliente c : clientes) {
            Object[] fila = {
                c.getNombre(),
                c.getDireccion(),
                c.getCorreo(),
                c.getTelefono()
            };
            modeloTablaClientes.addRow(fila);
        }
    }

    // Métodos para Proveedores
    private void registrarProveedor() {
        String nombre = txtNombreProv.getText().trim();
        String direccion = txtDireccionProv.getText().trim();
        String correo = txtCorreoProv.getText().trim();
        String telefono = txtTelefonoProv.getText().trim();
        
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El nombre es obligatorio", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Proveedor proveedor = new Proveedor(nombre, direccion, correo, telefono);
        inventarioDAO.agregarProveedor(proveedor);
        
        JOptionPane.showMessageDialog(this, 
            "Proveedor registrado exitosamente", 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
        
        txtNombreProv.setText("");
        txtDireccionProv.setText("");
        txtCorreoProv.setText("");
        txtTelefonoProv.setText("");
        
        cargarProveedoresEnTabla();
    }

    private void cargarProveedoresEnTabla() {
        modeloTablaProveedores.setRowCount(0);
        List<Proveedor> proveedores = inventarioDAO.obtenerTodosLosProveedores();
        
        for (Proveedor p : proveedores) {
            Object[] fila = {
                p.getNombre(),
                p.getDireccion(),
                p.getCorreo(),
                p.getTelefono()
            };
            modeloTablaProveedores.addRow(fila);
        }
    }

    // Métodos para Ventas
    private void actualizarComboProductos() {
        comboProductos.removeAllItems();
        List<Producto> productos = inventarioDAO.obtenerTodosLosProductos();
        
        for (Producto p : productos) {
            comboProductos.addItem(p.getCodigo() + " - " + p.getDescripcion() + 
                                 " (Stock: " + p.getCantidad() + ")");
        }
    }

    private void actualizarComboClientes() {
        comboClientes.removeAllItems();
        List<Cliente> clientes = inventarioDAO.obtenerTodosLosClientes();
        
        for (Cliente c : clientes) {
            comboClientes.addItem(c.getNombre());
        }
    }

    private void registrarVenta() {
        try {
            int indexProd = comboProductos.getSelectedIndex();
            int indexCli = comboClientes.getSelectedIndex();
            
            if (indexProd == -1 || indexCli == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Seleccione producto y cliente", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int cantidad = Integer.parseInt(txtCantidadVenta.getText().trim());
            
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "La cantidad debe ser mayor a 0", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<Producto> productos = inventarioDAO.obtenerTodosLosProductos();
            Producto productoSeleccionado = productos.get(indexProd);
            
            // Validar stock disponible
            if (productoSeleccionado.getCantidad() < cantidad) {
                JOptionPane.showMessageDialog(this, 
                    "Stock insuficiente. Disponible: " + productoSeleccionado.getCantidad(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<Cliente> clientes = inventarioDAO.obtenerTodosLosClientes();
            Cliente clienteSeleccionado = clientes.get(indexCli);
            
            String tipoCompra = radioLinea.isSelected() ? "linea" : "tienda";
            
            // Registrar venta
            Venta venta = inventarioDAO.registrarVenta(productoSeleccionado, 
                                                      clienteSeleccionado, 
                                                      cantidad, 
                                                      tipoCompra);
            
            if (venta != null) {
                // Mostrar detalle de venta
                StringBuilder detalle = new StringBuilder();
                detalle.append("=== DETALLE DE VENTA ===\n\n");
                detalle.append("ID Venta: ").append(venta.getIdVenta()).append("\n");
                detalle.append("Producto: ").append(venta.getProducto().getDescripcion()).append("\n");
                detalle.append("Cliente: ").append(venta.getCliente().getNombre()).append("\n");
                detalle.append("Cantidad: ").append(venta.getCantidad()).append("\n");
                detalle.append("Precio Unitario: $").append(String.format("%.2f", venta.getPrecioUnitario())).append("\n");
                detalle.append("Subtotal: $").append(String.format("%.2f", venta.getCantidad() * venta.getPrecioUnitario())).append("\n");
                
                double descuento = venta.getDescuentoAplicado();
                if (descuento > 0) {
                    detalle.append("Descuento (15%): -$").append(String.format("%.2f", descuento)).append("\n");
                    detalle.append("¡Descuento aplicado por compra en línea > 3 productos!\n");
                }
                
                detalle.append("\nTOTAL: $").append(String.format("%.2f", venta.getTotal())).append("\n");
                detalle.append("Tipo: ").append(venta.getTipoCompra().equalsIgnoreCase("linea") ? 
                                               "En Línea" : "Tienda Física").append("\n");
                detalle.append("Fecha: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(venta.getFecha()));
                
                txtReporteVenta.setText(detalle.toString());
                
                JOptionPane.showMessageDialog(this, 
                    "Venta registrada exitosamente\nTotal: $" + String.format("%.2f", venta.getTotal()), 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // Actualizar tablas y combos
                cargarProductosEnTabla();
                cargarVentasEnTabla();
                actualizarComboProductos();
                
                txtCantidadVenta.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Ingrese una cantidad válida", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarVentasEnTabla() {
        modeloTablaVentas.setRowCount(0);
        List<Venta> ventas = inventarioDAO.obtenerTodasLasVentas();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        for (Venta v : ventas) {
            Object[] fila = {
                v.getIdVenta(),
                v.getProducto().getDescripcion(),
                v.getCliente().getNombre(),
                v.getCantidad(),
                String.format("$%.2f", v.getTotal()),
                v.getTipoCompra().equalsIgnoreCase("linea") ? "En Línea" : "Tienda",
                sdf.format(v.getFecha())
            };
            modeloTablaVentas.addRow(fila);
        }
    }

    // Métodos para Reportes
    private void generarReporteInventario() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("========================================\n");
        reporte.append("   REPORTE DE INVENTARIO\n");
        reporte.append("   Tienda de Electrónicos\n");
        reporte.append("   Fecha: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date())).append("\n");
        reporte.append("========================================\n\n");
        
        // Listar productos
        reporte.append("PRODUCTOS REGISTRADOS:\n");
        reporte.append("----------------------------------------\n");
        List<Producto> productos = inventarioDAO.obtenerTodosLosProductos();
        
        for (Producto p : productos) {
            reporte.append("Código: ").append(p.getCodigo()).append("\n");
            reporte.append("Descripción: ").append(p.getDescripcion()).append("\n");
            reporte.append("Cantidad: ").append(p.getCantidad()).append(" unidades\n");
            reporte.append("Precio: $").append(String.format("%.2f", p.getPrecio())).append("\n");
            reporte.append("Ubicación: ").append(p.getUbicacion()).append("\n");
            reporte.append("----------------------------------------\n");
        }
        
        // Valor total del inventario
        double valorTotal = inventarioDAO.obtenerValorTotalInventario();
        reporte.append("\nVALOR TOTAL DEL INVENTARIO: $").append(String.format("%.2f", valorTotal)).append("\n");
        
        // Productos bajo stock
        reporte.append("\n========================================\n");
        reporte.append("PRODUCTOS CON STOCK BAJO (<= 10):\n");
        reporte.append("========================================\n");
        List<Producto> bajoStock = inventarioDAO.obtenerProductosBajoStock(10);
        
        if (bajoStock.isEmpty()) {
            reporte.append("No hay productos con stock bajo.\n");
        } else {
            for (Producto p : bajoStock) {
                reporte.append(p.getDescripcion()).append(" - Stock: ").append(p.getCantidad()).append("\n");
            }
        }
        
        // Estadísticas de ventas
        reporte.append("\n========================================\n");
        reporte.append("ESTADÍSTICAS DE VENTAS:\n");
        reporte.append("========================================\n");
        List<Venta> ventas = inventarioDAO.obtenerTodasLasVentas();
        reporte.append("Total de ventas registradas: ").append(ventas.size()).append("\n");
        
        double totalVentas = 0;
        for (Venta v : ventas) {
            totalVentas += v.getTotal();
        }
        reporte.append("Monto total vendido: $").append(String.format("%.2f", totalVentas)).append("\n");
        
        txtReporteInventario.setText(reporte.toString());
    }

    public static void main(String[] args) {
        // Ejecutar en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            InventarioElectronicaGUI ventana = new InventarioElectronicaGUI();
            ventana.setVisible(true);
        });
    }
}